package kb_hack.backend.domain.festival.service;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.reactive.function.client.WebClient;
//
// @Slf4j
// @Service
// public class FestivalApiService {
//
//     private final WebClient webClient;
//     private final String serviceKey;
//
//     // @Qualifier를 사용하여 정확한 WebClient Bean을 주입받습니다.
//     public FestivalApiService(@Qualifier("tourApiWebClient") WebClient webClient,
//         @Value("${tour.api.service-key}") String serviceKey) {
//         this.webClient = webClient;
//         this.serviceKey = serviceKey;
//     }
//
//     /**
//      * 지역 코드 조회 API 호출 (areaCode2)
//      * @return API 응답 결과 (JSON 문자열)
//      */
//     public String getAreaCodes() {
//         log.info("Requesting area codes from Tour API...");
//
//         // WebClientConfig에서 설정한 baseUrl과 공통 파라미터가 자동으로 적용됩니다.
//         return
//             WebClient.builder()
//                 .uriBuilderFactory(uriBuilderFactory)
//                 .baseUrl(festivalUrl)
//                 .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(30 * 1024 * 1024))     //DataBufferLimitException 해결
//                 .build()
//                 .get()
//                 .uri(uriBuilder -> uriBuilder
//                     .path("/areaCode2")
//                     .queryParam("serviceKey", this.serviceKey) // 여기서 직접 키를 추가!) // 상세 경로만 지정
//                     .build()
//                 )
//                 .retrieve() // 요청 실행 및 응답 수신
//                 .bodyToMono(String.class) // 응답 본문을 String으로 변환
//                 .block(); // 비동기 결과를 동기적으로 대기
//     }
//
//     /**
//      * 행사 정보 검색 API 호출 (searchFestival2)
//      * @param eventStartDate 행사 시작일 (예: "20250830")
//      * @param areaCode 지역 코드 (예: "35")
//      * @return API 응답 결과 (JSON 문자열)
//      */
//     public String searchFestivals(String eventStartDate, String areaCode) {
//         log.info("Searching festivals for startDate: {}, areaCode: {}", eventStartDate, areaCode);
//
//         return webClient.get()
//                 .uri(uriBuilder -> uriBuilder
//                         .path("/searchFestival2")
//                         .queryParam("eventStartDate", eventStartDate) // 이 요청에만 필요한 파라미터 추가
//                         .queryParam("areaCode", areaCode) // 이 요청에만 필요한 파라미터 추가
//                         .build())
//                 .retrieve()
//                 .bodyToMono(String.class)
//                 .block();
//     }
// }
