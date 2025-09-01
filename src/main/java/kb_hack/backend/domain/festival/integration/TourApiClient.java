// domain/festival/integration/TourApiClient.java
package kb_hack.backend.domain.festival.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kb_hack.backend.domain.festival.Festival;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourApiClient {

    @Qualifier("tourApiWebClient")
    private final WebClient webClient;

    @Value("${tour.api.service-key}")
    private String serviceKey;


    public Mono<JsonNode> searchFestival2(String areaCode, String fromYmd, int page, int size) {
        return webClient.get()
                .uri(uri -> uri.path("/searchFestival2")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("MobileOS", "WEB")
                        .queryParam("MobileApp", "mobile")
                        .queryParam("_type", "json")
                        .queryParam("eventStartDate", fromYmd)
                        .queryParam("pageNo", page)
                        .queryParam("numOfRows", size)
                        .queryParam("areaCode", areaCode)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class);
    }

    /** 상세: detailCommon2 **/
    public Mono<Festival> fetchOverview(String contentId) {
        return webClient.get()
                .uri(uri -> uri.path("/detailCommon2")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("MobileOS", "WEB")
                        .queryParam("MobileApp", "mobile")
                        .queryParam("_type", "json")
                        .queryParam("contentId", contentId)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(body -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper(); //json 파서
                        JsonNode root = mapper.readTree(body); // 문자열을 트리 형태로 변환
                        JsonNode items = root.path("response")
                                .path("body")
                                .path("items")
                                .path("item");
                        JsonNode n = items.isArray() ? items.get(0) : items;
                        if (n == null || n.isMissingNode()) {
                            log.warn("No detail item found for contentId={}", contentId);
                            return Mono.empty();
                        }

                        Festival festival = new Festival();
                        festival.setContentId(contentId);

                        String overview = n.path("overview").asText(null);
                        if (overview != null) {
                            overview = overview.replaceAll("<[^>]+>", "").replace("&nbsp;", " ").trim();
                            festival.setOverview(overview);
                        }

                        String telname = n.path("telname").asText(null);
                        festival.setTelName(telname);

//                        log.info("OVERVIEW: {}, TELNAME: {}", overview, telname);

                        return Mono.just(festival);
                    } catch (Exception e) {
                        log.error("파싱 실패 (contentId={}): {}", contentId, e.getMessage());
                        return Mono.empty(); //
                    }
                });
    }
}
