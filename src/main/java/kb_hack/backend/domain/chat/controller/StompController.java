package kb_hack.backend.domain.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;


import kb_hack.backend.domain.chat.dto.ChatMessageDto;
import kb_hack.backend.domain.chat.entity.ChatMessage;
import kb_hack.backend.domain.chat.mapper.ReadStatusMapper;
import kb_hack.backend.domain.chat.service.ChatService;
import kb_hack.backend.domain.chat.service.ReadStatusService;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class StompController {
	private final SimpMessageSendingOperations messageTemplate;

	private final ChatService chatService;
	private final ReadStatusService readStatusService;

	@MessageMapping("/{roomId}")
	public void sendMessage(@DestinationVariable Long roomId,
		@Payload ChatMessageDto chatMessageDto,
		StompHeaderAccessor accessor){
		// 1. StompHeaderAccessor에서 사용자 정보(Principal)를 직접 꺼냅니다.
		// 이 사용자 정보는 Handshake 단계에서 우리가 직접 설정해준 것입니다.
		Authentication authentication = (Authentication) accessor.getUser();

		// 2. Principal을 실제 사용자 타입으로 캐스팅합니다.
		SecurityCustomUser customUser = (SecurityCustomUser) authentication.getPrincipal();


		log.info("메시지 수신 성공: room={}, user={}, payload={}", roomId, customUser.getUsername(), chatMessageDto);
		ChatMessage savedMessage = chatService.saveMessage(roomId, chatMessageDto, customUser);
		// 메시지를 보낸 사용자의 읽음 상태를 업데이트합니다.
		readStatusService.markMessagesAsRead(roomId, customUser.getMemberVO().getMemberId());

		// 3. 브로드캐스팅할 DTO를 생성합니다. (history API와 동일한 DTO 사용)
		ChatMessageResponse responseDto = ChatMessageResponse.builder()
			.chatMessageId(savedMessage.getChatMessageId())
			.senderId(savedMessage.getSenderId())
			.senderEmail(customUser.getMemberVO().getMemberEmail()) // 인증된 사용자 정보에서 이메일을 가져옵니다.
			.content(savedMessage.getContent())
			.createdAt(savedMessage.getCreatedAt()) // LocalDateTime을 String으로 변환
			.build();

		messageTemplate.convertAndSend("/topic/" + roomId, responseDto);

	}



}
