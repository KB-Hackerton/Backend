package kb_hack.backend.domain.chat.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import kb_hack.backend.domain.chat.dto.ChatMessageDto;
import kb_hack.backend.domain.chat.service.ChatService;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@AllArgsConstructor
@Data
@Builder
public class ChatMessageResponse {
	private Long chatMessageId;
	private Long senderId;
	private String senderEmail;
	private int isRead;
	private String content;
	private LocalDateTime createdAt;
}
