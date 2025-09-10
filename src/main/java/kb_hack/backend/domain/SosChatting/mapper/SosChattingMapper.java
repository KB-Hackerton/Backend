package kb_hack.backend.domain.SosChatting.mapper;

import kb_hack.backend.domain.SosChatting.dto.ChattingMessageInsertParam;
import kb_hack.backend.domain.SosChatting.dto.ChattingMessageItem;
import kb_hack.backend.domain.SosChatting.dto.ChattingRoomListItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SosChattingMapper {
    List<ChattingRoomListItem> selectRoomListWithLastAndUnread(@Param("myId") long myId);

    // --- 시나리오 2) 방 입장 ---
    int updateReadUpToOthersMax(@Param("roomId") long roomId, @Param("myId") long myId);

    List<ChattingMessageItem> selectLatestMessages(@Param("roomId") long roomId, @Param("size") int size);

    List<ChattingMessageItem> selectMessagesBefore(@Param("roomId") long roomId, @Param("beforeId") long beforeId, @Param("size") int size);

    Long selectOtherMemberId(@Param("roomId") long roomId, @Param("myId") long myId);

    // --- 시나리오 3) 내가 메시지 전송 ---
    int insertMessage(ChattingMessageInsertParam p); // useGeneratedKeys

    int bumpMyReadToNew(@Param("roomId") long roomId, @Param("myId") long myId, @Param("newId") long newId);

    int bumpOtherReadToNew(@Param("roomId") long roomId, @Param("otherId") long otherId, @Param("newId") long newId);

    boolean selectIsReadByOther(@Param("roomId") long roomId, @Param("otherId") long otherId, @Param("newId") long newId);

    boolean isMemberInRoom(@Param("roomId") long roomId, @Param("myId") long myId);
}
