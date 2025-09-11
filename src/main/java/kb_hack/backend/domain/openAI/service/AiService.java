package kb_hack.backend.domain.openAI.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AiService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public AiService(@Qualifier("openAiWebClient") WebClient webClient) {
        this.webClient = webClient;
        this.objectMapper = new ObjectMapper();
    }

    public Mono<String> askOpenAi(String userInput) {
        Map<String, String> systemMsg = Map.of(
                "role", "system",
                "content", "당신은 소상공인 지원사업 전문 컨설턴트입니다. 사용자 사업장 정보와 제공된 공고지원사업 목록을 분석하여 " +
                        "가장 적합한 공고 하나를 찾아 그 공고의 id만 답변해 주세요. 다른 말은 필요없고 id만 숫자로 반환하세요. json으로 대답하니까 {\"id\":30}겠지"
        );

        Map<String, String> userMsg = Map.of("role", "user", "content", userInput);

        List<Map<String, String>> messages = List.of(systemMsg, userMsg);
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");
        body.put("messages", messages);

        log.info("GPT 요청 본문: {}", body);

        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(responseBody -> {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                        String content = message.get("content").toString().trim();
                        log.info("📢 GPT 모델 응답 내용: '{}'", content);

                        try {
                            Map<String, Object> jsonResponse = objectMapper.readValue(content, Map.class);
                            if (jsonResponse.containsKey("id")) {
                                return jsonResponse.get("id").toString();
                            }
                        } catch (JsonProcessingException e) {
                            log.error("JSON 파싱 오류: {}", content, e);
                        }
                    }
                    log.error("OpenAI 응답에 유효한 'choices'가 없습니다.");
                    return null;
                })
                .doOnError(e -> log.error("GPT 호출 중 예외", e))
                .onErrorResume(e -> Mono.empty());
    }
}
