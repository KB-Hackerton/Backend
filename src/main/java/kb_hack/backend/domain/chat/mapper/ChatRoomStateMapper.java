package kb_hack.backend.domain.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import kb_hack.backend.domain.chat.entity.ChatRoomState;

@Mapper
public interface ChatRoomStateMapper {

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
}
