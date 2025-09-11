package kb_hack.backend.domain.chat.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyChatListResponse {
	private Long roomId;
	private String roomName;
	private Long unReadCount;
	private String lastMessage;
	private LocalDateTime lastMessageTime;
	private String bussinessName;
	private String memberProfileImage;
	private Long ownerId;
}
