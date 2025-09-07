package kb_hack.backend.domain.chat.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

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
}
