package kb_hack.backend.domain.chat.entity;

import java.time.LocalDateTime;

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
public class ChatRoomState {

	private Long chatRoomStateId;

	private Long chatRoomId;

	private Long memberId;

	private Long lastReadMessageId;

	private LocalDateTime createdAt;


}
