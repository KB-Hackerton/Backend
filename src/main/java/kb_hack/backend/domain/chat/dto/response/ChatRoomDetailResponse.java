package kb_hack.backend.domain.chat.dto.response;

import java.time.LocalDateTime;

import kb_hack.backend.domain.sos.entity.SosType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class ChatRoomDetailResponse {
	private String roomName;       // 채팅방 이름
	private SosType sosType;        // SOS 유형
	private String memberBadge;    // 멤버 뱃지
	private Boolean isComplete;     // SOS 완료 여부
	private Boolean isOwner;      	// 현재 사용자가 SOS 소유자인지 여부
	private LocalDateTime createdAt;  // 채팅방 생성 시간
	private String partnerImage; // 상대방 프로필 이미지 URL
	private int unReadCount; // 읽지 않은 메시지 수
}
