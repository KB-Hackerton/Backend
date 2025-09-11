package kb_hack.backend.domain.SosChatting.service;

import kb_hack.backend.domain.SosChatting.dto.*;
import kb_hack.backend.domain.SosChatting.mapper.SosChattingMapper;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.CustomException;
import kb_hack.backend.global.common.exception.type.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class SosChattingService{
    private final SosChattingMapper sosChattingMapper;

    //        return sosChattingMapper.selectRoomListWithLastAndUnread(myId);
    //채팅방 리스트에서 채팅방 가져오기
    @Transactional(readOnly = true)
    public List<ChattingRoomListItem> getChatRooms(long myId) {

        // 1. 내가 속한 채팅방 목록 (roomId, roomName만 가져옴)
        List<ChattingRoomListItem> rooms = sosChattingMapper.selectMyChatRooms(myId);
        if (rooms.isEmpty()) {
            return rooms;
        }

        // 채팅방 ID만 추출
        List<Long> roomIds = rooms.stream()
                .map(ChattingRoomListItem::getChatRoomId)
                .toList();

        // 2. 각 방의 마지막 메시지 가져오기
        List<ChattingRoomListItem> lastMessages = sosChattingMapper.selectLastMessages(roomIds);
        Map<Long, ChattingRoomListItem> lastMessageMap = lastMessages.stream()
                .collect(Collectors.toMap(ChattingRoomListItem::getChatRoomId, lm -> lm));

        // 3. 각 방의 안 읽은 메시지 개수 가져오기
        List<UnreadCountDTO> unreadCounts = sosChattingMapper.selectUnreadCounts(myId, roomIds);
        Map<Long, Integer> unreadMap = unreadCounts.stream()
                .collect(Collectors.toMap(UnreadCountDTO::getChatRoomId, UnreadCountDTO::getUnreadCount));

        // 4. 상대방 프로필 가져오기
        List<OtherProfileDTO> otherProfiles = sosChattingMapper.selectOtherMemberProfiles(myId, roomIds);
        Map<Long, String> profileMap = otherProfiles.stream()
                .collect(Collectors.toMap(
                        OtherProfileDTO::getChatRoomId,
                        OtherProfileDTO::getProfileImageUrl,
                        (a, b) -> a // 같은 roomId면 첫 값 유지
                ));

        // 5. 결과 병합
        for (ChattingRoomListItem room : rooms) {
            ChattingRoomListItem lm = lastMessageMap.get(room.getChatRoomId());
            if (lm != null) {
                room.setLastMessageId(lm.getLastMessageId());
                room.setLastMessageSenderId(lm.getLastMessageSenderId());
                room.setLastMessageContent(lm.getLastMessageContent());
                room.setLastMessageAt(lm.getLastMessageAt());
            }
            room.setUnreadCount(unreadMap.getOrDefault(room.getChatRoomId(), 0));
            room.setOtherProfileImageUrl(profileMap.get(room.getChatRoomId()));
        }

        // 마지막 메시지 시간 기준으로 정렬 (최신순)
        rooms.sort(Comparator.comparing(
                ChattingRoomListItem::getLastMessageAt,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        return rooms;
    }



    //방 입장시 상대방 메세지 읽음 표시
    public void markOthersMessagesAsRead(long roomId, long myId) {
        int updated = sosChattingMapper.updateReadUpToOthersMax(roomId, myId);
        log.info("⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐⭐Room {}: {} messages marked as read for member {}", roomId, updated, myId);
    }

    // 방에 처음 입장할 때 최신 메시지 묶음을 가져올 때 사용
    @Transactional(readOnly = true)
    public List<ChattingMessageItem> getLatestMessages(long roomId, int size) {
        return sosChattingMapper.selectLatestMessages(roomId, size);
    }

    // 프론트에서 위로 스크롤할 때,
    //“현재 화면에서 가장 위 메시지 id = beforeId” 기준으로 그 앞쪽 메시지(size 개) 를 더 불러오기.
    @Transactional(readOnly = true)
    public List<ChattingMessageItem> getMessagesBefore(long roomId, long beforeId, int size) {
        return sosChattingMapper.selectMessagesBefore(roomId, beforeId, size);
    }

    // 상대방 member id 가져오기
    @Transactional(readOnly = true)
    public Long getOtherMemberId(long roomId, long myId) {
        return sosChattingMapper.selectOtherMemberId(roomId, myId);
    }

    //이거 로직 뭐야 ?
    public ChattingMessageItem sendMessage(long roomId, long myId, String content) {
        System.out.println("⭐⭐⭐⭐⭐roomId = " + roomId);
        System.out.println("⭐⭐⭐⭐⭐myId = " + myId);
        System.out.println("⭐⭐⭐⭐⭐content = " + content);
        // 1. 메시지 저장
        ChattingMessageInsertParam param = ChattingMessageInsertParam.builder()
                .roomId(roomId)
                .senderId(myId)
                .content(content)
                .build();

        int result = sosChattingMapper.insertMessage(param);
        if (result < 1) {
            throw new RuntimeException("메시지 저장 실패");
        }

        long newId = param.getId();

        // 2. 내 포인터를 새 메시지로 업데이트
        sosChattingMapper.bumpMyReadToNew(roomId, myId, newId);

//        // 3. (옵션) 상대방이 방 안에 있으면 즉시 읽음 처리
//        Long otherId = getOtherMemberId(roomId, myId);
//        if (otherId != null) {
//            sosChattingMapper.bumpOtherReadToNew(roomId, otherId, newId);
//        }
//
//        // 4. 읽음 여부 확인
//        boolean readByOther = false;
//        if (otherId != null) {
//            readByOther = sosChattingMapper.selectIsReadByOther(roomId, otherId, newId);
//        }

        return ChattingMessageItem.builder()
                .id(newId)
                .senderId(String.valueOf(myId))
                .content(content)
                .createdAt(null) // createdAt은 selectLatestMessages 호출 때 가져올 수도 있음
                .build();
    }

    @Transactional(readOnly = true)
    public void validateRoomMembership(long roomId, long myId) {
        boolean isMember = sosChattingMapper.isMemberInRoom(roomId, myId);
        if (!isMember) {
            throw new ForbiddenException(BadStatusCode.CHAT_ROOM_ACCESS_DENIED);

        }
    }




}
