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
public class ReadStatus {

	private Long readStatusId;

	private Long chatRoomId;

	private Long chatMessageId;

	private Long memberId;

	private Boolean isRead; // 읽음 상태 (true: 읽음, false: 안읽음)

	private LocalDateTime createdAt;
}
