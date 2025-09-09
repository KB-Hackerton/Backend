package kb_hack.backend.domain.chat.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kb_hack.backend.domain.chat.entity.ReadStatus;

@Mapper
public interface ReadStatusMapper {

	@Insert("""
    INSERT INTO read_status (
       chat_room_id, chat_message_id, member_id, is_read
    ) VALUES (
       #{chatRoomId}, #{chatMessageId}, #{memberId}, #{isRead}
    )
""")
	@Options(useGeneratedKeys = true, keyProperty = "readStatusId", keyColumn = "read_status_id")
	int save(ReadStatus readStatus);

	@Update("""
	UPDATE read_status
	SET chat_message_id = #{lastMessageId}, is_read = 1
	WHERE chat_room_id = #{roomId} AND member_id = #{memberId}
	""")
	int updateLastReadMessage(Long roomId, Long memberId, Long lastMessageId);

	@Update("""
	UPDATE read_status
	SET is_read = 1
	WHERE chat_room_id = #{roomId} AND member_id = #{memberId} AND is_read = 0
	""")
	int markAsRead(Long roomId, Long memberId);

	@Select("""

	SELECT COUNT(*)
	FROM read_status rs
	JOIN chat_message cm ON rs.chat_message_id = cm.chat_message_id
	WHERE rs.chat_room_id = #{chatRoomId}
	  AND rs.member_id = #{memberId}
	  AND rs.is_read = 0
	""")
	Long countUnreadMessages(Long chatRoomId, Long memberId);
}
