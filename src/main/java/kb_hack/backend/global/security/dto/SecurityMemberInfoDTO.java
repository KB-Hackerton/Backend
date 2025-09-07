package kb_hack.backend.global.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import kb_hack.backend.domain.business.dto.BusinessDTO;
import kb_hack.backend.global.security.entity.MemberAuthVO;
import kb_hack.backend.global.security.entity.MemberVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SecurityMemberInfoDTO {
    private Long memberId;
    private Long profileImageId;
    private String memberEmail;
    private String memberName;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date createdAt;
    private Integer helpCount;
    private String badge;
    private String minorNm;
    private BusinessDTO businessDTO;
    private List<MemberAuthVO> authMap;

    public static SecurityMemberInfoDTO convertToDTO(MemberVO vo) {
        return SecurityMemberInfoDTO.builder()
                .memberId(vo.getMemberId())
                .profileImageId(vo.getProfileImageId())
                .memberEmail(vo.getMemberEmail())
                .memberName(vo.getMemberName())
                .createdAt(vo.getCreatedAt())
                .helpCount(vo.getHelpCount())
                .badge(vo.getBadge())
                .businessDTO(vo.getBusinessDTO())
                .authMap(vo.getAuthMap())
                .build();
    }
}