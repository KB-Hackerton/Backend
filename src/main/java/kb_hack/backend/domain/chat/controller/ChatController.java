package kb_hack.backend.domain.chat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import kb_hack.backend.domain.chat.dto.request.RoomCreateRequest;
import kb_hack.backend.domain.chat.dto.response.ChatMessageHistoryDto;
import kb_hack.backend.domain.chat.dto.response.MyChatListResponse;
import kb_hack.backend.domain.chat.entity.ChatRoom;
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

	@Operation(
		summary = "1:1 채팅방 생성",
		description = "특정 SOS와 관련된 1:1 채팅방을 생성하거나, 이미 존재하는 경우 해당 채팅방 ID를 반환합니다."
	)
	@PostMapping("/room/private/create")
	public ResponseEntity<Long> getOrCreatePrivateChatRoom(@RequestBody RoomCreateRequest request,
	@AuthenticationPrincipal SecurityCustomUser customUser) {
		Long chatRoomId = chatService.
			createPrivateChatRoom(customUser.getMemberVO(), request.getSosId());

		return ResponseEntity.ok().body(chatRoomId);
	}

	//----------------------------------------------------------------------------------------------------------------------

	@Operation(
		summary = "채팅방 대화 내역 조회",
		description = "특정 채팅방의 대화 내역을 조회합니다. 사용자는 해당 채팅방의 참여자여야 합니다."
	)
	@GetMapping("/history/{roomId}")
	public ResponseEntity<?> getChatHistory(@PathVariable Long roomId,
		@AuthenticationPrincipal SecurityCustomUser customUser) {

		List<ChatMessageHistoryDto> chatHistory =
			chatService.getChatHistory(roomId, customUser.getMemberVO().getMemberId());

		return ResponseEntity.ok(chatHistory);
	}










	@Operation(
		summary = "채팅방 메시지 읽음 처리",
		description = "특정 채팅방의 모든 메시지를 읽음 처리합니다. 사용자는 해당 채팅방의 참여자여야 합니다."
	)
	@PostMapping("/room/{roomId}/read")
	public ResponseEntity<Void> markAsRead(@PathVariable Long roomId,
		@AuthenticationPrincipal SecurityCustomUser customUser) {

		chatService.markMessagesAsRead(roomId, customUser.getMemberVO().getMemberId());
		return ResponseEntity.ok().build();
	}











	@Operation(
		summary = "채팅방 상세 정보 조회",
		description = "특정 채팅방의 상세 정보를 조회합니다. 사용자는 해당 채팅방의 참여자여야 합니다."
	)
	@GetMapping("/room/detail/{roomId}")
	public ResponseEntity<?> getChatDetail(@PathVariable Long roomId,
		@AuthenticationPrincipal SecurityCustomUser customUser) {
		ChatRoom chatRoomDetail = chatService.getChatRoomDetail(roomId, customUser.getMemberVO());
		return ResponseEntity.ok(chatRoomDetail);
	}










	@Operation(
		summary = "내가 속한 채팅방 목록 조회",
		description = "현재 로그인한 사용자가 속한 모든 채팅방의 목록을 조회합니다."
	)
	@GetMapping("/my/rooms")
	public ResponseEntity<?> getMyChatRooms(@AuthenticationPrincipal SecurityCustomUser customUser) {
		List<MyChatListResponse> chatRooms = chatService.getMyChatRooms(customUser.getMemberVO());
		return ResponseEntity.ok(chatRooms);
	}











	@Operation(
		summary = "채팅방 나가기",
		description = "특정 채팅방에서 나갑니다. 사용자는 해당 채팅방의 참여자여야 합니다."
	)
	@PostMapping("/room/{roomId}/leave")
	public ResponseEntity<?> leaveChatRoom(@PathVariable Long roomId,
		@AuthenticationPrincipal SecurityCustomUser customUser) {
		chatService.leaveChatRoom(roomId, customUser.getMemberVO());
		return ResponseEntity.ok().build();
	}


}
