package kb_hack.backend.domain.document.dto;

import java.time.LocalDate;

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

	private String announceTitle; // 공고 제목
	private LocalDate reqstStartDate;
	private LocalDate reqstEndDate;   // announce.reqst_end_date
	private int totalDocs;        // 총 서류 개수
	private int checkedDocs;      // 체크한 서류 개수
}
