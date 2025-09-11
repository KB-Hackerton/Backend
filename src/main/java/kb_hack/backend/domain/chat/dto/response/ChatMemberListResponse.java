package kb_hack.backend.domain.chat.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChatMemberListResponse {
	private Long memberId;
	private String memberName;
	private String imageURL;
}
