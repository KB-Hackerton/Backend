package kb_hack.backend.domain.sos.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import kb_hack.backend.domain.sos.entity.SosType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class SosDetailResponse {

	@Schema(description = "SOS ID", example = "101")
	private Long sosId;

	@Schema(description = "가게 이름", example = "동욱타이어")
	private String businessName;

	@Schema(description = "회원 뱃지", example = "도움 촌장")
	private String badge;

	@Schema(description = "가게 주소", example = "서울특별시 동작구")
	private String businessAddr;

	@Schema(description = "가게 상세 주소", example = "어딘가에 있을것 같아요")
	private String businessAddrDetail;

	@Schema(description = "SOS 요청 제목", example = "긴급 타이어 지원 요청")
	private String sosTitle;

	@Schema(description = "SOS 요청 카테고리", example = "stock")
	private SosType sosType;

	@Schema(description = "SOS 요청 내용", example = "급하게 타이어가 필요합니다.")
	private String sosContent;

	@Schema(description = "SOS 종료 시각", example = "2025-09-07T18:00:00")
	private LocalDateTime expiresAt;

	private Date createdAt;

	@Schema(description = "SOS 이미지 스토리지 키 리스트",
		example = "[\"sos/2025/09/01/uuid1.jpg\", \"sos/2025/09/01/uuid2.png\"]")
	private List<String> imageKeys;
}
