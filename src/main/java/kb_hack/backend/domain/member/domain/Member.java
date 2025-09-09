package kb_hack.backend.domain.member.domain;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Member {
    private Long memberId;
    private Long businessId;
    private Long profileImageId;
    private String memberEmail;
    private String password;
    private String memberName;
    private Date createdAt;
    private Integer helpCount;
    private String badge;
}
