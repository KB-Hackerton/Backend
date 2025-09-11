package kb_hack.backend.domain.sos.dto;


import kb_hack.backend.domain.sos.entity.SosType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SosDetailRow {
	private Long sosId;
	private Long businessId;
	private String businessName;       // business.business_nm
	private String badge;              // member.badge
	private String businessAddr;       // business.business_addr
	private String businessAddrDetail; // business.business_addr_detail
	private String sosTitle;
	private SosType sosType;
	private String sosContent;
	private LocalDateTime expiresAt;
	private Date createdAt;
	private String imageKey;           // sos_image.storage_key
	private Long sosImageId;

}
