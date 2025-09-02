package kb_hack.backend.global.security.mapper;

import kb_hack.backend.global.security.entity.MemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    public MemberVO getMemberByMemberEmail(String memberEmail);
}
