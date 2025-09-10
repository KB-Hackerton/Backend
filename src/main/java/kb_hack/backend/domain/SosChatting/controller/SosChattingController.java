package kb_hack.backend.domain.SosChatting.controller;

import kb_hack.backend.domain.SosChatting.dto.ChattingMessageItem;
import kb_hack.backend.domain.SosChatting.dto.ChattingRoomListItem;
import kb_hack.backend.domain.SosChatting.service.SosChattingService;
import kb_hack.backend.domain.chat.dto.request.RoomCreateRequest;
import kb_hack.backend.domain.chat.service.ChatService;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/sos-chat")
public class SosChattingController {
    private final SosChattingService sosChattingService;
    private final ChatService chatService;

    // 시나리오 1) 채팅 탭 진입 → 채팅방 리스트 조회
    @GetMapping
    public ResponseEntity<List<ChattingRoomListItem>> getChatRooms(
            @AuthenticationPrincipal SecurityCustomUser user) {
        long myId = user.getMemberVO().getMemberId();
        List<ChattingRoomListItem> rooms = sosChattingService.getChatRooms(myId);
        return ResponseEntity.ok(rooms);
    }

    // 시나리오 2) 방 입장 → 메시지 조회 + 읽음 처리
    //beforeId 가 없으면 최신 메시지(size개), 있으면 해당 메시지 이전(size개)
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChattingMessageItem>> getMessages(
            @PathVariable long roomId,
            @RequestParam(required = false) Long beforeId,
            @RequestParam(defaultValue = "30") int size,
            @AuthenticationPrincipal SecurityCustomUser user) {

        long myId = user.getMemberVO().getMemberId();

        // 방 입장 시 1회: 상대 메시지 읽음 처리
        sosChattingService.markOthersMessagesAsRead(roomId, myId);

        List<ChattingMessageItem> messages;
        if (beforeId == null) {
            messages = sosChattingService.getLatestMessages(roomId, size);
        } else {
            messages = sosChattingService.getMessagesBefore(roomId, beforeId, size);
        }
        return ResponseEntity.ok(messages);
    }

    // 시나리오 3) 메시지 전송
    @PostMapping("/{roomId}/messages")
    public ResponseEntity<ChattingMessageItem> sendMessage(
            @PathVariable long roomId,
            @RequestParam String content,
            @AuthenticationPrincipal SecurityCustomUser user) {

        long myId = user.getMemberVO().getMemberId();
        ChattingMessageItem message = sosChattingService.sendMessage(roomId, myId, content);
        return ResponseEntity.ok(message);
    }

    //채팅방 생성
    @PostMapping("")
    public ResponseEntity<Long> getOrCreatePrivateChatRoom(@RequestBody RoomCreateRequest request,
                                                           @AuthenticationPrincipal SecurityCustomUser customUser) {
        Long chatRoomId = chatService.createPrivateChatRoom(customUser.getMemberVO(), request.getSosId());

        return ResponseEntity.ok().body(chatRoomId);
    }
}
