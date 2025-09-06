package kb_hack.backend.domain.member.service;

import kb_hack.backend.domain.business.dto.BusinessDTO;
import kb_hack.backend.domain.business.mapper.BusinessMapper;
import kb_hack.backend.domain.member.dto.request.MemberInfoRequestDTO;
import kb_hack.backend.domain.member.mapper.MemberMapper;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.BadRequestException;
import kb_hack.backend.global.common.exception.type.ServerErrorException;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberInfoService {
    private final MemberMapper memberMapper;
    private final BusinessMapper businessMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void updatePassword(String OriginalPassword, String newPassword){
        MemberVO vo = getMemberVO();
        if (!bCryptPasswordEncoder.matches(OriginalPassword, vo.getPassword())) {
            throw new BadRequestException(BadStatusCode.INCORRECT_ORIGINAL_PASSWORD_EXCEPTION);
        }
        System.out.println("vo.getPassword() = " + vo.getPassword());
        String encode = bCryptPasswordEncoder.encode(newPassword);
        int i = memberMapper.updatePasswordByMemberEmail(vo.getMemberEmail(), encode);

        if (i == 0) {
            throw new ServerErrorException(BadStatusCode.DATABASE_PROCESSING_EXCEPTION);
        }
    }

    @Transactional
    public void updateMemberInfo(MemberInfoRequestDTO memberInfoRequestDTO){
        MemberVO vo = getMemberVO();
        long businessClassId = businessMapper.findBusinessClassIdByMinorname(memberInfoRequestDTO.getMinorName());
        if (businessClassId <= 0) {
            throw new BadRequestException(BadStatusCode.INVALID_MINOR_NAME_EXCEPTION);
        }
        memberInfoRequestDTO.setBusinessClassId(businessClassId);

        int i = businessMapper.updateBusiness(memberInfoRequestDTO, vo.getBusinessDTO().getBusinessId());
        if (i == 0) {
            throw new ServerErrorException(BadStatusCode.DATABASE_PROCESSING_EXCEPTION);
        }

        int updateMember = memberMapper.updateMembernameByMemberId(memberInfoRequestDTO.getMemberName(), vo.getMemberId());
        if (updateMember == 0) {
            throw new ServerErrorException(BadStatusCode.DATABASE_PROCESSING_EXCEPTION);
        }

    }

    private static MemberVO getMemberVO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityCustomUser securityUser = (SecurityCustomUser) authentication.getPrincipal();
        MemberVO vo = securityUser.getMemberVO();
        return vo;
    }


}
