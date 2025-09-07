package kb_hack.backend.domain.member.dto;

import kb_hack.backend.domain.member.dto.request.SigunUpRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long memberId;
    private Long profileImageId;    // FK → profile_image.profile_image_id
    private String memberEmail;
    private String password;
    private String memberName;
    private String kakaoId;
    private String status;
    private String fcmToken;
    private Integer helpCount;
    private String badge;

    public static MemberDTO convertToMemberDTO(SigunUpRequestDTO dto){
        return MemberDTO.builder()
                .memberEmail(dto.getMemberEmail())
                .password(dto.getPassword())
                .memberName(dto.getMemberName())
                .kakaoId(dto.getKakaoId())
                .status("active")
                .helpCount(0)
                .badge("도움 일꾼")
                .build();
    }

}
