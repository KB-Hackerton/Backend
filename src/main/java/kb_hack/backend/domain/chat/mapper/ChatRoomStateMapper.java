package kb_hack.backend.domain.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;

import kb_hack.backend.domain.chat.entity.ChatRoom;
import kb_hack.backend.domain.chat.entity.ChatRoomState;

@Mapper
public interface ChatRoomStateMapper {

	@Update("""
    UPDATE chat_room_state
    SET last_read_message_id = #{lastMessageId}
    WHERE chat_room_id = #{roomId} AND member_id = #{memberId}
""")
	int updateLastReadMessage(Long roomId, Long memberId, Long lastMessageId);

	@Insert("""
			INSERT INTO chat_room_state (chat_room_id, member_id, last_read_message_id)
			VALUES (#{chatRoomId}, #{memberId}, #{lastReadMessageId})
		""")
	@Options(useGeneratedKeys = true, keyProperty = "chatRoomStateId", keyColumn = "chat_room_state_id")
	int save(ChatRoomState chatRoomState);

	@Select("""
		SELECT *
		FROM chat_room_state
		WHERE chat_room_id = #{chatRoomId}
	""")

	List<ChatRoomState> findByChatRoom(Long chatRoomId);
	@Select("""
		SELECT cr.*
		FROM chat_room cr
		JOIN chat_room_state crs ON cr.chat_room_id = crs.chat_room_id
		WHERE crs.member_id = #{memberId}
	""")
	List<ChatRoom> findChatRoomsByMemberId(Long memberId);

	@Select("""
		SELECT cr.*
		FROM chat_room cr
		JOIN chat_room_state crs ON cr.chat_room_id = crs.chat_room_id
		WHERE crs.member_id = #{memberId} AND cr.sos_id = #{sosId}
	""")
	ChatRoom findChatRoomsByMemberIdAndSosId(Long memberId, Long sosId);
}
