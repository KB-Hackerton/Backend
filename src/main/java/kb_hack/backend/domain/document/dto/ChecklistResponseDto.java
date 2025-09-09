package kb_hack.backend.domain.document.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistResponseDto {
	private Long announceId;
	private String announceTitle;
	private List<DocumentItemDto> checklist;
}
