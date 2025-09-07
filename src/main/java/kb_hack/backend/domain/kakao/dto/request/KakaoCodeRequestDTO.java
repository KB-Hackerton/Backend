package kb_hack.backend.domain.kakao.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class KakaoCodeRequestDTO {
    @Schema(description = "카카오 Code 값을 입력해주세요", example = "psrvCkOcN-BgowCOdzJvKNKfPDbS2Kljo212Te0iUWBwctbqgDc2hwAAAAQKDSBaAAABmSO6fa0WphHJzwXJqw")
    private String code;
}
