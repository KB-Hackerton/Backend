package kb_hack.backend.domain.document.dto;

import lombok.Data;

@Data
public class DocumentCheckItemDto {
	private Long documentId;
	private boolean checked;
}
