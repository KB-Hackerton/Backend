package kb_hack.backend.domain.sos.dto;

import kb_hack.backend.domain.sos.entity.SosType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SosDetailResponse {
	private Long sosId;
	private String businessName;
	private String badge;
	private String businessAddr;
	private String businessAddrDetail;
	private String sosTitle;
	private SosType sosType;
	private String sosContent;
	private LocalDateTime expiresAt;
	private List<String> imageKeys;  // sos_image.storage_key 리스트
}
