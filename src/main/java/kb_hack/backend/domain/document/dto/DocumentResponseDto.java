package kb_hack.backend.domain.document.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentResponseDto {
	private Long documentId;
	private Long announceId;
	private String title;
	private String description;
	private boolean checked;
}
