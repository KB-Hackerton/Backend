package kb_hack.backend.domain.document.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DocumentResponseDto {
	private Long documentId;
	private Long announceId;
	private String title;
	private String description;
	private boolean checked;
}
