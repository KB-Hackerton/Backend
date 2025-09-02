package kb_hack.backend.global.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kb_hack.backend.global.security.entity.MemberAuthVO;
import kb_hack.backend.global.security.entity.MemberVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfoDTO {
    private Long memberId;
    private Long businessId;
    private Long profileImageId;
    private String memberEmail;
    private String memberName;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date createdAt;
    private Integer helpCount;
    private String badge;
    private List<MemberAuthVO> authMap;

    public static MemberInfoDTO convertToDTO(MemberVO vo) {
        return MemberInfoDTO.builder()
                .memberId(vo.getMemberId())
                .businessId(vo.getBusinessId())
                .profileImageId(vo.getProfileImageId())
                .memberEmail(vo.getMemberEmail())
                .memberName(vo.getMemberName())
                .createdAt(vo.getCreatedAt())
                .helpCount(vo.getHelpCount())
                .badge(vo.getBadge())
                .authMap(vo.getAuthMap())
                .build();
    }
}