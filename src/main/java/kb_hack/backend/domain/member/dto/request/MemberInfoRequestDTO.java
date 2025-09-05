package kb_hack.backend.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberInfoRequestDTO {
//    private String image;
    @JsonIgnore
    private Long businessClassId;

    @Schema(description = "사업자 등록 번호", example = "12-34-566")
    private String businessCode;

    @Schema(description = "대표자 성명", example = "이동욱")
    private String memberName;

    @Schema(description = "상호명", example = "동욱 아이스크림 가게")
    private String businessNm;

    @Schema(description = "개업 연월일", example = "2025-09-06")
    private Date businessOpenDate;

    @Schema(description = "업종-소분류 이름 ", example = "한복 소매업")
    private String minorName;

    @Schema(description = "사업자 주소", example = "서울특별시 강남구")
    private String businessAddr;

    @Schema(description = "사업자 상세 주소", example = "12334동 123124호")
    private String businessAddrDetail;






}
