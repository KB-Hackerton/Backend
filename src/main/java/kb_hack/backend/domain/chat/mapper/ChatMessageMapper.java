package kb_hack.backend.domain.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ChatMessageMapper {

	// 특정 채팅방에서 마지막으로 읽은 메시지 ID 이후의 메시지 개수 세기
	@Select("""
	SELECT COUNT(*)
	FROM chat_message
	WHERE chat_room_id = #{roomId} AND chat_message_id > #{lastReadMessageId}
	""")
	 int countMessagesAfterId(Long roomId, Long lastReadMessageId);

}
