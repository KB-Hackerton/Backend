package kb_hack.backend.domain.chat.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import kb_hack.backend.domain.chat.controller.ChatMessageResponse;
import kb_hack.backend.domain.chat.entity.ChatMessage;
import kb_hack.backend.domain.chat.entity.ChatRoom;

@Mapper
public interface ChatRoomMapper {

	ChatRoom findByRoomId(Long roomId);

	// private Long chatRoomId;
	// 	private String roomName;
	// 	private Long sosId;
	// 	private SosType sosType;
	// 	private int isComplete; // 0: 진행중, 1: 완료
	// 	private Long ownerId;
	@Insert("""
		INSERT INTO chat_room (sos_id, room_name, room_type, is_complete, owner_id)
		VALUES (#{sosId}, #{roomName}, #{roomType}, #{isComplete}, #{ownerId} )
	""")
	@Options(useGeneratedKeys = true, keyProperty = "chatRoomId", keyColumn = "chat_room_id")
	int save(ChatRoom newChatRoom);

	// create table chat_message
	// (
	//     chat_message_id bigint auto_increment
	//         primary key,
	//     chat_room_id    bigint                             not null,
	//     sender_id       varchar(100)                       null,
	//     content         text                               null,
	//     created_at      datetime default CURRENT_TIMESTAMP not null,
	//     constraint fk_chat_message_room
	//         foreign key (chat_room_id) references chat_room (chat_room_id)
	//             on update cascade on delete cascade
	// );
	@Select("""
		SELECT cm.chat_message_id, cm.chat_room_id, cm.sender_id, cm.content, cm.created_at
		FROM chat_message cm
		WHERE cm.chat_room_id = #{roomId}
		ORDER BY cm.created_at ASC
""")
	List<ChatMessage> getChatMessagesByRoomId(Long roomId);

	@Insert("""
		INSERT INTO chat_message (chat_room_id, sender_id, content)
		VALUES (#{chatRoomId}, #{senderId}, #{content})
	""")
	@Options(useGeneratedKeys = true, keyProperty = "chatMessageId", keyColumn = "chat_message_id")
	int saveMessage(ChatMessage chatMessage);

}
