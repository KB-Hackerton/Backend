package kb_hack.backend.domain.sos.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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
public class SosCreateRequest {

	@NotNull
	@Schema(description = "요청 생성한 사용자 ID", example = "1")
	private Long memberId;

	@Size(max = 255)
	@Schema(description = "SOS 요청 제목 (선택 가능)", example = "급히 타이어 구합니다")
	private String sosTitle; // nullable

	@NotNull
	@Schema(description = "SOS 요청 카테고리", example = "stock")
	private SosType sosType;

	@NotBlank
	@Schema(description = "SOS 요청 상세 내용", example = "오늘 안에 타이어 4개 필요합니다. 급하게 도와주실 분?")
	private String sosContent;

	@NotBlank
	@Schema(description = "요청 만료 시각 (yyyy-MM-dd HH:mm 또는 HH:mm)", example = "2025-09-07 23:59")
	private String expiresAt;

	@Schema(description = "첨부 이미지 파일 리스트", example = "[image1.jpg, image2.jpg]")
	private List<MultipartFile> images;
}
