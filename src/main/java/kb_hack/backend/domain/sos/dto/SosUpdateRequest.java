package kb_hack.backend.domain.sos.dto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kb_hack.backend.domain.sos.entity.SosType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SosUpdateRequest {

	@Schema(description = "SOS 요청 제목", example = "급히 타이어 구합니다")
	private String sosTitle;

	@NotNull
	@Schema(description = "SOS 요청 카테고리", example = "stock")
	private SosType sosType;

	@NotBlank
	@Schema(description = "SOS 요청 상세 내용", example = "오늘 안에 타이어 4개 필요합니다")
	private String sosContent;

	@NotBlank
	@Schema(description = "요청 만료 시각 (yyyy-MM-dd HH:mm 또는 HH:mm)", example = "2025-09-07 23:59")
	private String expiresAt;

	@Schema(description = "삭제할 이미지 ID 리스트", example = "[1,2]")
	private List<Long> deleteImageIds;

	@Schema(description = "새로 추가할 이미지 파일 리스트")
	private List<MultipartFile> newImages;
}
