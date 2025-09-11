package kb_hack.backend.domain.chatbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1/chat/completions")
            .build();

    @Value("${openai.api.key}")
    private String OPENAI_API_KEY;

    public String askOpenAi(String userInput) {
        try {
            Map<String, String> systemMsg = Map.of(
                    "role", "system",
                    "content", String.join("\n",
                            "너는 지역 소상공인을 돕는 전문 챗봇이야.",
                            "지원금·행사 캘린더, 맞춤 알림, SOS 네트워크, 서류 관리 같은 기능에 대해 잘 알고 있어야 해.",
                            "사용자가 묻는 내용은 소상공인 지원제도, 대출·지원금 정보, 지역 축제, 재고 나눔 등과 관련돼.",
                            "어려운 행정 용어는 풀어서 설명하고, 필요하면 간단한 예시를 들어 쉽게 안내해줘.",
                            "말투는 친근하면서도 정중하게, 지역 소상공인과 상담하는 듯한 느낌으로 해.",
                            "질문이 모호하면 '조금 더 구체적으로 말씀해주시면 더 정확히 도와드릴 수 있어요.'라고 알려줘.",
                            "설명은 너무 길지 않게, 핵심이 잘 전달되도록 간결하게 정리해.",
                            "목표는 소상공인이 필요한 정보를 빠르게 찾고, 서로 협력할 수 있도록 돕는 것이야."
                    )
            );
            Map<String, String> userMsg = Map.of("role", "user", "content", userInput);

            List<Map<String, String>> messages = List.of(systemMsg, userMsg);
            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-4o");
            body.put("messages", messages);

            Map<String, Object> responseBody = webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + OPENAI_API_KEY)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return message.get("content").toString().trim();
                }
            }
            return "OpenAI 응답이 비어 있습니다.";

        } catch (Exception e) {
            log.error("GPT 호출 중 예외", e);
            return "GPT 응답 중 오류가 발생했습니다.";
        }
    }
}
