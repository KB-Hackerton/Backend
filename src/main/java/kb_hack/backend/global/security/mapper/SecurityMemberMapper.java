package kb_hack.backend.global.security.mapper;

import kb_hack.backend.global.security.entity.MemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SecurityMemberMapper {
     MemberVO getMemberByMemberEmail(String memberEmail);
     String getMinorNmByBusinessId(Long businessId);
}
