package kb_hack.backend.global.security.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class MemberVO {
    private Long memberId;
    private Long businessId;
    private Long profileImageId;
    private String memberEmail;
    private String password;
    private String memberName;
    private Date createdAt;
    private Integer helpCount;
    private String badge;
    private List<MemberAuthVO> authMap;
}