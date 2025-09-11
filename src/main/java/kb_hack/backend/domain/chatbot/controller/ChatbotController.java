package kb_hack.backend.domain.chatbot.controller;

import kb_hack.backend.domain.chatbot.dto.ChatRequest;
import kb_hack.backend.domain.chatbot.service.ChatbotService;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
public class ChatbotController {
    private final ChatbotService chatbotService;

    @PostMapping
    public SuccessResponse<String> handleChat(@RequestBody ChatRequest request) {
        String userInput = request.getMessage();
        String aiAnswer = chatbotService.askOpenAi(userInput);
        return SuccessResponse.makeResponse(SuccessStatusCode.GET_CHAT_SUCEESS, aiAnswer);
    }
}
