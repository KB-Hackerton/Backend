package kb_hack.backend.domain.sos.dto;
import kb_hack.backend.domain.sos.entity.SosType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SosListResponse {
	private Long sosId;
	private String businessName;    // business.business_nm
	private String sosTitle;        // sos.sos_title
	private SosType sosType;        // sos.sos_type
	private LocalDateTime expiresAt;// sos.expires_at
}
