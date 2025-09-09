package kb_hack.backend.global.security.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import kb_hack.backend.domain.business.dto.BusinessDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberVO {
    private Long memberId;
    private String storageKey;
    private String memberEmail;
    private String password;
    private String memberName;
    private Date createdAt;
    private Integer helpCount;
    private String badge;
    private String minorNm;
    private BusinessDTO businessDTO;
    private List<MemberAuthVO> authMap;
}