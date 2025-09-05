package kb_hack.backend.domain.member.mapper;

import kb_hack.backend.domain.member.dto.AuthDTO;
import kb_hack.backend.domain.member.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    int insertMember(MemberDTO memberDTO);
    int insertAuth(AuthDTO authDTO);
    int deleteUser(Long memberId);
    int updatePasswordByMemberEmail(@Param("memberEmail") String memberEmail, @Param("newPassword")String newPassword);
    int updateMembernameByMemberId(@Param("memberName") String memberName, @Param("memberId") Long memberId);

}
