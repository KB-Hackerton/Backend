package kb_hack.backend.domain.chat.service;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kb_hack.backend.domain.chat.dto.ChatMessageDto;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class StompController {
	private final SimpMessageSendingOperations messageTemplate;

	private final ChatService chatService;


	@MessageMapping("/{roomId}")
	public void sendMessage(@DestinationVariable Long roomId, ChatMessageDto chatMessageDto
	,@AuthenticationPrincipal SecurityCustomUser customUser) throws
		JsonProcessingException {
		System.out.println(chatMessageDto.getMessage());
		chatService.saveMessage(roomId, chatMessageDto);

		chatMessageDto.setRoomId(roomId);
		// chatMessageDto.setCreatedTime(LocalDateTime.now());
		messageTemplate.convertAndSend("/topic/" + roomId, chatMessageDto);

	}
}
