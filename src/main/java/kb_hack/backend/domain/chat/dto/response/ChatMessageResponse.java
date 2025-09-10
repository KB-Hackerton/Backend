package kb_hack.backend.domain.chat.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@AllArgsConstructor
@Data
@Builder
public class ChatMessageResponse {
	private Long chatMessageId;
	private Long senderId;
	private String senderEmail;
	private int isRead;
	private Long unreadCount; // 이 메시지를 안 읽은 사람의 수 (0 또는 1)
	private String content;
	private LocalDateTime createdAt;
}
