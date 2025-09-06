package kb_hack.backend.domain.sos.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SosCreateResponse {

	@Schema(description = "생성된 SOS 요청 ID", example = "101")
	private Long sosId;

	@Schema(description = "저장된 이미지의 스토리지 키 리스트",
		example = "[\"sos/2025/09/01/uuid1.jpg\", \"sos/2025/09/01/uuid2.png\"]")
	private List<String> imageKeys;
}
