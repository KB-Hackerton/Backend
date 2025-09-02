package kb_hack.backend.domain.announce.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.announce.mapper.AnnounceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class BizinfoApiService {

    private final WebClient webClient;
    private final AnnounceMapper announceMapper;

    @Value("${bizinfo.api.key}")
    private String crtfcKey;

    public BizinfoApiService(
            @Qualifier("bizinfoWebClient") WebClient webClient,
            AnnounceMapper announceMapper
    ) {
        this.webClient = webClient;
        this.announceMapper = announceMapper;
    }

    public int fetchAndSaveBizinfo() throws Exception{
        String result =webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/uss/rss/bizinfoApi.do")
                        .queryParam("crtfcKey", crtfcKey)
                        .queryParam("dataType", "json")
                        .queryParam("pageUnit",200)
                        .queryParam("pageIndex", "3")
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(result);
        JsonNode items = root.path("jsonArray");
        int count =0;

        for(JsonNode item : items){
            LocalDate startDate = null;
            LocalDate endDate = null;
            String reqstBeginEndDe =item.path("reqstBeginEndDe").asText(null);

            if (reqstBeginEndDe != null && reqstBeginEndDe.contains("~")) {
                String[] parts = reqstBeginEndDe.split("~");
                if (parts.length == 2) {
                    // Ensure the date parts are cleaned before parsing
                    String startStr = parts[0].trim().replaceAll("[^0-9]", "");
                    String endStr = parts[1].trim().replaceAll("[^0-9]", "");
                    if (startStr.length() >= 8 && endStr.length() >= 8) {
                        startDate = LocalDate.parse(startStr.substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd"));
                        endDate = LocalDate.parse(endStr.substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd"));
                    }
                }
            }

            LocalDate pubDate = null;
            String creatPnttm = item.path("creatPnttm").asText(null);
            if (creatPnttm != null) {
                // Correctly remove non-digit characters and parse
                String digitsOnly = creatPnttm.replaceAll("[^0-9]", "");
                if (digitsOnly.length() >= 8) {
                    String dateStr = digitsOnly.substring(0, 8);
                    pubDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
                }
            }


            Announce announce = Announce.builder()
                    .announceTitle(item.path("pblancNm").asText(null))
                    .link(item.path("pblancUrl").asText(null))
                    .author(item.path("jrsdInsttNm").asText(null))
                    .excInsttNm(item.path("excInsttNm").asText(null))
                    .description(item.path("bsnsSumryCn").asText(null))
                    .lcategory(item.path("pldirSportRealmLclasCodeNm").asText(null))
                    .pubDate(pubDate)
                    .startDate(startDate)
                    .endDate(endDate)
                    .targetName(item.path("trgetNm").asText(null))
                    .viewNum(item.path("inqireCo").asInt(0))
                    .filePathName(item.path("flpthNm").asText(null))
                    .fileName(item.path("fileNm").asText(null))
                    .howToRegister(item.path("reqstMthPapersCn").asText(null))
                    .callCompany(item.path("refrncNm").asText(null))
                    .build();

            announceMapper.insertAnnounce(announce);
            count++;
        }
return count;
    }
}
