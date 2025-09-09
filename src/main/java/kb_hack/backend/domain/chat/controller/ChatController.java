package kb_hack.backend.domain.chat.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kb_hack.backend.domain.chat.dto.request.RoomCreateRequest;
import kb_hack.backend.domain.chat.service.ChatService;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/chat")
@RestController
public class ChatController {

	private final ChatService chatService;

	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}


	/**
	 * 1:1 채팅방 생성
	 * @param request
	 * @param customUser
	 * @return
	 */
	@PostMapping("/room/private/create")
	public ResponseEntity<?> getOrCreatePrivateChatRoom(@RequestBody RoomCreateRequest request,
	@AuthenticationPrincipal SecurityCustomUser customUser) {
		customUser.getMemberVO();

		chatService.createPrivateChatRoom(customUser.getMemberVO(), request.getSosId(), request.getOtherMemberId());
		return ResponseEntity.ok().build();
	}

	// 이전 메시지 조회
	@GetMapping("/history/{roomId}")
	public ResponseEntity<?> getChatHistory(@PathVariable Long roomId,
		@AuthenticationPrincipal SecurityCustomUser customUser) {

		List<ChatMessageResponse> chatHistory =
			chatService.getChatHistory(roomId, customUser.getMemberVO().getMemberId());

		return ResponseEntity.ok(chatHistory);
	}


	@PostMapping("/chat/room/{roomId}/read")
	public ResponseEntity<Void> markAsRead(@PathVariable Long roomId,
		@AuthenticationPrincipal SecurityCustomUser customUser) {

		chatService.markMessagesAsRead(roomId, customUser.getMemberVO().getMemberId());
		return ResponseEntity.ok().build();
	}

}
