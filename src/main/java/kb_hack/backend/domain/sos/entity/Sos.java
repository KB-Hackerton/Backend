package kb_hack.backend.domain.sos.entity;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Sos {
	private Long sosId;
	private Long memberId;
	private String sosTitle;           // nullable
	private SosType sosType;
	private String sosContent;
	private LocalDateTime expiresAt;
	private Boolean isComplete;        // default false (코드에서 세팅)
	private Boolean isDeleted;         // default false (코드에서 세팅)
	private LocalDateTime createdAt;   // DB default
}
