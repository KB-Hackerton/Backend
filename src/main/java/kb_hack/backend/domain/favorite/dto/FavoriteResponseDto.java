package kb_hack.backend.domain.favorite.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class FavoriteResponseDto {
	private Long announceId;
	private String announceTitle;
	private LocalDate deadline;   // announce.reqst_end_date
	private int totalDocs;        // 총 서류 개수
	private int checkedDocs;      // 체크한 서류 개수
}

