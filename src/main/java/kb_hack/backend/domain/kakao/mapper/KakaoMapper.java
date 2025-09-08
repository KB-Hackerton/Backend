package kb_hack.backend.domain.kakao.mapper;

import kb_hack.backend.global.security.entity.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface KakaoMapper {
    int existsByKakaoId( Long kakaoId);
    int existByMemberEmail(String memberEmail);

    MemberVO findByKakaoId(Long id);

    int updateKakaoIdByEmail(@Param("email") String email, @Param("kakaoId")Long kakaoId);

}
