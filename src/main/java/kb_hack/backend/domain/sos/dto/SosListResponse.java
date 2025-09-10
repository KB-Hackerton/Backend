package kb_hack.backend.domain.sos.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import kb_hack.backend.domain.sos.entity.SosType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SosListResponse {

	@Schema(description = "SOS ID", example = "101")
	private Long sosId;

	private Long memberId;

	@Schema(description = "가게 이름", example = "동욱타이어")
	private String businessName;    // business.business_nm

	@Schema(description = "SOS 요청 제목", example = "긴급 타이어 지원 요청")
	private String sosTitle;        // sos.sos_title

	@Schema(description = "SOS 요청 카테고리", example = "stock")
	private SosType sosType;        // sos.sos_type

	@Schema(description = "SOS 생성 시간", example = "12:12")
	private Date createdAt;

	@Schema(description = "SOS 종료 시각", example = "2025-09-07T18:00:00")
	private LocalDateTime expiresAt; // sos.expires_at

	@Schema(description = "가게 주소", example = "서울특별시 동작구~~")
	private String businessAddr;

	@Schema(description = "가게 상세 주소", example = "101호")
	private String businessAddrDetail;

	@Schema(description = "프로필 이미지", example = "https:~~~~~")
	private String imageURL;
}
