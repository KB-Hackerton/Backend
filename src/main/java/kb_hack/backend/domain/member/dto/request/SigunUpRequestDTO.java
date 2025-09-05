package kb_hack.backend.domain.member.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SigunUpRequestDTO {
    private String memberEmail;
    private String password;
    private String memberName;
    private String businessCode;
    private String businessNm;
    private Date businessOpenDate;
    private String businessAddr;
    private String businessAddrDetail;
    private String minorName;


}
