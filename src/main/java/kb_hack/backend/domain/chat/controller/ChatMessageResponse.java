package kb_hack.backend.domain.chat.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class ChatMessageResponse {
	private Long chatMessageId;

	private Long senderId;

	private String content;

	private String createdAt;
}
