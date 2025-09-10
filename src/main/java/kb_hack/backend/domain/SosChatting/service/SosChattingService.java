package kb_hack.backend.domain.SosChatting.service;

import kb_hack.backend.domain.SosChatting.dto.ChattingMessageInsertParam;
import kb_hack.backend.domain.SosChatting.dto.ChattingMessageItem;
import kb_hack.backend.domain.SosChatting.dto.ChattingRoomListItem;
import kb_hack.backend.domain.SosChatting.mapper.SosChattingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class SosChattingService{
    private final SosChattingMapper sosChattingMapper;

    //채팅방 리스트에서 채팅방 가져오기
    @Transactional(readOnly = true)
    public List<ChattingRoomListItem> getChatRooms(long myId) {
        return sosChattingMapper.selectRoomListWithLastAndUnread(myId);
    }

    //방 입장시 상대방 메세지 읽음 표시
    public void markOthersMessagesAsRead(long roomId, long myId) {
        int updated = sosChattingMapper.updateReadUpToOthersMax(roomId, myId);
        log.info("Room {}: {} messages marked as read for member {}", roomId, updated, myId);
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

        // 3. (옵션) 상대방이 방 안에 있으면 즉시 읽음 처리
        Long otherId = getOtherMemberId(roomId, myId);
        if (otherId != null) {
            sosChattingMapper.bumpOtherReadToNew(roomId, otherId, newId);
        }

        // 4. 읽음 여부 확인
        boolean readByOther = false;
        if (otherId != null) {
            readByOther = sosChattingMapper.selectIsReadByOther(roomId, otherId, newId);
        }

        return ChattingMessageItem.builder()
                .id(newId)
                .senderId(String.valueOf(myId))
                .content(content)
                .createdAt(null) // createdAt은 selectLatestMessages 호출 때 가져올 수도 있음
                .build();
    }




}
