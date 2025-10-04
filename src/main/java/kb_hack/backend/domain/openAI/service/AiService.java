package kb_hack.backend.domain.openAI.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kb_hack.backend.domain.openAI.dto.AiRecommendationRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//webclinet => openAI 요청 => 동기적 결과 기다림
@Service
@Slf4j
public class AiService {

    @Value("${openai.api.model}")
    private String apiModel;

    private final WebClient webClient;

    public AiService(@Qualifier("openAiWebClient") WebClient webClient,  @Value("${openai.api.model}") String apiModel) {
        this.webClient = webClient;
        this.apiModel = apiModel;
    }

    public String getAiResponseId(String fullPrompt) {

        Map<String, String> systemMsg = Map.of(
                "role", "system", "content",
                " 당신은 소상공인 지원사업 전문 컨설턴트입니다. 사용자 사업장 정보와 제공된 공고지원사업 목록을 분석하여 가장 적합한 공고 하나를 찾아 그 공고의 id만 답변해 주세요. 다른 말은 필요없고 id만 숫자로 반환하세요."
        );
        Map<String, String> userMsg = Map.of("role", "user", "content", fullPrompt);

        //요청 DTO 생성
        AiRecommendationRequestDto request = AiRecommendationRequestDto.builder()
                .model(apiModel)
                .input(List.of(systemMsg, userMsg))
                .temperature(0.5f)
                .build();

        try{
            Map responseBody = webClient.post()
                    .bodyValue(request) //DTO -> JSON 으로 변환해서 전송
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(); // 동기적 기다림
            // 응답 파싱
            if(responseBody.containsKey("output") && responseBody !=null){
                List<Map<String,Object>> output =
                        (List<Map<String, Object>>) responseBody.get("output");
                if(output != null && output.size() > 0){
                    List<Map<String, Object>> contentList = (List<Map<String, Object>>) output.get(0).get("content");

                    String responseId = (String) contentList.get(0).get("text");
                    log.info("⭐추천 id응답: {}", responseId);
                    return responseId.trim();
                }
            }
        }catch (Exception e){
            log.error("OpenaI 호출 중 오류 발생", e);
            return null;
        }
    return null;}
}