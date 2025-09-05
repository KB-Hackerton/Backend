package kb_hack.backend.domain.business.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import kb_hack.backend.domain.member.dto.request.SigunUpRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BusinessDTO {
    private Long businessId;// AUTO_INCREMENT PK
    @JsonIgnore
    private Long memberId;
    private Long businessClassId;
    private String businessNm;
    private String businessAddr;
    private String businessAddrDetail;
    private String businessCode;
    private Date businessOpenDate;
    private String si;
    private String sigungu;
    private String eupmyeonDong;

    public static BusinessDTO makeBusniessDTO(SigunUpRequestDTO dto){
        return BusinessDTO.builder()
                .businessNm(dto.getBusinessNm())
                .businessAddr(dto.getBusinessAddr())
                .businessAddrDetail(dto.getBusinessAddrDetail())
                .businessCode(dto.getBusinessCode())
                .businessOpenDate(dto.getBusinessOpenDate())
                .build();
    }

}
