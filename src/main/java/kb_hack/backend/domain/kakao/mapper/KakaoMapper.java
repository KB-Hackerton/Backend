package kb_hack.backend.domain.kakao.mapper;

import kb_hack.backend.global.security.entity.MemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KakaoMapper {
    int existsByKakaoId( Long kakaoId);

    MemberVO findByKakaoId(Long id);
}
