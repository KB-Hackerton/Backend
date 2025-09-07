package kb_hack.backend.domain.chat.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatMessage {
	private Long chatMessageId;

	private Long chatRoomId;

	private Long senderId;

	private String content;

	private String createdAt;

}
