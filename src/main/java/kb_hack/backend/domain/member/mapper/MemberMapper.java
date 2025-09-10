package kb_hack.backend.domain.member.mapper;

import kb_hack.backend.domain.member.domain.Member;
import kb_hack.backend.domain.member.dto.AuthDTO;
import kb_hack.backend.domain.member.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    int insertMember(MemberDTO memberDTO);
    int insertAuth(AuthDTO authDTO);
    int deleteUser(Long memberId);
    int updatePasswordByMemberEmail(@Param("memberEmail") String memberEmail, @Param("newPassword")String newPassword);
    int updateMembernameByMemberId(@Param("memberName") String memberName, @Param("memberId") Long memberId);
    Member getMemberByEmail(String email);
    Member getMemberByMemberId(Long memberId);
    Long getMemberIdByEmail(@Param("memberEmail") String email);
    List<Long> getAllMemberId();

    int incrementHelpCount(Long helperId);
}
