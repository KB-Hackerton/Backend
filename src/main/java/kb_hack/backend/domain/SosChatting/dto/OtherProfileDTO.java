package kb_hack.backend.domain.SosChatting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtherProfileDTO {
    private long chatRoomId;
    private long memberId;
    private String profileImageUrl;
    private String businessName;
}
