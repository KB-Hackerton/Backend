package kb_hack.backend.domain.chat.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.*;

import kb_hack.backend.domain.chat.dto.response.ChatMessageHistoryDto;
import kb_hack.backend.domain.chat.dto.response.ChatMessageResponse;
import kb_hack.backend.domain.chat.dto.response.MyChatListResponse;
import kb_hack.backend.domain.chat.entity.ChatMessage;

import kb_hack.backend.domain.chat.entity.ChatRoom;

@Mapper
public interface ChatRoomMapper {

	ChatRoom findByRoomId(Long roomId);

	@Insert("""
		INSERT INTO chat_room (sos_id, room_name, room_type, is_complete, owner_id)
		VALUES (#{sosId}, #{roomName}, #{roomType}, #{isComplete}, #{ownerId} )
	""")
	@Options(useGeneratedKeys = true, keyProperty = "chatRoomId", keyColumn = "chat_room_id")
	int save(ChatRoom newChatRoom);

	List<ChatMessageResponse> getChatMessagesByRoomId(Long roomId);

	@Insert("""
		INSERT INTO chat_message (chat_room_id, sender_id, content)
		VALUES (#{chatRoomId}, #{senderId}, #{content})
	""")
	@Options(useGeneratedKeys = true, keyProperty = "chatMessageId", keyColumn = "chat_message_id")
	int saveMessage(ChatMessage chatMessage);

	// roomId로 메시지 목록을 가져올 때, 보낸 사람의 이메일도 함께 가져오는 메소드
	List<ChatMessageHistoryDto> findChatHistoryWithSenderEmailByRoomId(Long memberId, Long roomId);

	ChatMessage findMessageById(Long chatMessageId);


	int leaveChatRoom(Long roomId, Long memberId);

	// 마지막 메시지 ID 가져오기
	@Select("""
    SELECT MAX(chat_message_id)
    FROM chat_message
    WHERE chat_room_id = #{roomId}
""")
	Long findLastMessageId(Long roomId);



	List<ChatRoom> findAllBySosId(Long sosId);


	int updateIsComplete(Long chatRoomId);


}
