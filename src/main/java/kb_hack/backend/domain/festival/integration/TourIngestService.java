package kb_hack.backend.domain.festival.integration;

import com.fasterxml.jackson.databind.JsonNode;
import kb_hack.backend.domain.festival.Festival;
import kb_hack.backend.domain.festival.mapper.FestivalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TourIngestService {
    private final FestivalMapper festivalMapper;
    private final TourApiClient client;

    public Mono<Integer> ingestGyeongsang() {
        final String fromYmd = "20250101";
        final int pageSize   = 200;
        final List<String> areas = List.of("35", "36","4","5","6","7");
        return Flux.fromIterable(areas)
                .concatMap(area -> ingestAllPages(area, fromYmd, pageSize))
                .reduce(0, Integer::sum);
    }

    /** 한 지역의  모든 페이지 수집 */
    private Mono<Integer> ingestAllPages(String areaCode, String fromYmd, int size){
        return fetchPage(areaCode, fromYmd, 1, size).flatMap(first -> {
            Mono<Integer> firstPageMono = processPage(first);
            if (first.total <= size) return firstPageMono;
            return Flux.range(2, (int)Math.ceil(first.total / (double)size) - 1)
                    .concatMap(p -> fetchPage(areaCode, fromYmd, p, size))
                    .concatMap(this::processPage)
                    .reduce(0, Integer::sum)
                    .flatMap(sum -> firstPageMono.map(firstSum -> sum + firstSum));
        });
    }

    /** 1페이지 수집 + 매핑 */
    private Mono<Page> fetchPage(String areaCode, String fromYmd, int page, int size){
        return client.searchFestival2(areaCode, fromYmd, page, size)
                .map(root -> {
                    JsonNode body = root.path("response").path("body");
                    int total = body.path("totalCount").asInt(0);
                    JsonNode items = body.path("items").path("item");
                    List<Festival> list = new ArrayList<>();
                    if (items.isArray()) {
                        for (JsonNode it : items) list.add(mapItem(it));
                    } else if (!items.isMissingNode() && !items.isNull()) {
                        list.add(mapItem(items));
                    }
                    return new Page(total, list);
                });
    }

    /** 페이지의 모든 아이템에 대해 상세 정보 가져와서 DB에 저장 */
    private Mono<Integer> processPage(Page page) {
        if (page.items.isEmpty()) return Mono.just(0);

        return Flux.fromIterable(page.items)
                .flatMap(f -> client.fetchOverview(f.getContentId()) // 호출
                        .map(detailedFestival -> {
                            // 상세 정보로 Festival 객체 업데이트
                            f.setOverview(detailedFestival.getOverview());
                            f.setTelName(detailedFestival.getTelName());
                            return f;
                        }))
                .collectList()
                .map(list -> {
                    if (list.isEmpty()) return 0;
                    return festivalMapper.batchUpsert(list);
                });
    }

    //festival api  정보 저장
    private static Festival mapItem(JsonNode it){
        Festival f = new Festival();
        f.setFestivalTitle(asText(it, "title"));
        f.setAdd1(asText(it, "addr1"));
        f.setAdd2(asText(it, "addr2"));
        f.setEventStartdate(parseYmd(asText(it, "eventstartdate")));
        f.setEventEnddate(parseYmd(asText(it, "eventenddate")));
        f.setFirstImage(asText(it, "firstimage"));
        f.setTel(asText(it, "tel"));
        f.setMapX(parseDecimal(asText(it, "mapx")));
        f.setMapY(parseDecimal(asText(it, "mapy")));
        f.setOverview(null);
        f.setTelName(null);
        f.setContentId(asText(it, "contentid"));
        return f;
    }

    private record Page(int total, List<Festival> items) {}

    // helpers
    private static String asText(JsonNode n, String f){ return n.path(f).asText(null); }
    private static LocalDate parseYmd(String y){
        if (y==null || y.length()!=8) return null;
        return LocalDate.of(
                Integer.parseInt(y.substring(0,4)),
                Integer.parseInt(y.substring(4,6)),
                Integer.parseInt(y.substring(6,8)));
    }
    private static BigDecimal parseDecimal(String s){
        try { return s==null? null : new BigDecimal(s); } catch (Exception e){ return null; }
    }
}
