package kb_hack.backend.domain.member.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SigunUpRequestDTO {
    @Schema(description = "회원 이메일", example = "test@example.com")
    private String memberEmail;

    @Schema(description = "비밀번호", example = "1234")
    private String password;

    @Schema(description = "회원 이름", example = "이동욱")
    private String memberName;

    @Schema(description = "사업자 등록번호", example = "123-456-7890")
    private String businessCode;

    @Schema(description = "가게 이름", example = "테스트 가게")
    private String businessNm;

    @Schema(description = "사업 시작일", example = "2025-09-05")
    private Date businessOpenDate;

    @Schema(description = "주소", example = "서울시 강남구")
    private String businessAddr;

    @Schema(description = "상세 주소", example = "101호")
    private String businessAddrDetail;

    @Schema(description = "업종(소분류)", example = "타이어 소매업")
    private String minorName;


}
