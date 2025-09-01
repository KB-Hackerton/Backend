package kb_hack.backend.domain.sos.entity;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class SosImage {
	private Long sosImageId;
	private Long sosId;
	private String storageKey;        // S3 경로/파일키 등
	private Boolean isDeleted;        // default false
	private LocalDateTime createdAt;  // DB default
}