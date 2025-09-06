package kb_hack.backend.domain.sos.dto;

import jakarta.validation.constraints.*;
import kb_hack.backend.domain.sos.entity.SosType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SosCreateRequest {

	@NotNull
	private Long memberId;

	@Size(max = 255)
	private String sosTitle; // nullable

	@NotNull
	private SosType sosType;

	@NotBlank
	private String sosContent;

	/**
	 * ISO-8601 문자열(예: "2025-09-02T23:59:00") 또는
	 * "yyyy-MM-dd HH:mm" 같은 포맷을 컨트롤러에서 파싱
	 */
	@NotBlank
	private String expiresAt;

	// 다중 이미지 업로드
	private List<MultipartFile> images;
}
