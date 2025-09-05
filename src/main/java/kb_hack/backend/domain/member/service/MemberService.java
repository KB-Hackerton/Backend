package kb_hack.backend.domain.member.service;

import kb_hack.backend.domain.business.dto.BusinessDTO;
import kb_hack.backend.domain.business.service.BusinessService;
import kb_hack.backend.domain.member.dto.AuthDTO;
import kb_hack.backend.domain.member.dto.MemberDTO;
import kb_hack.backend.domain.member.dto.request.SigunUpRequestDTO;
import kb_hack.backend.domain.member.mapper.MemberMapper;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.BadRequestException;
import kb_hack.backend.global.common.exception.type.ServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper ;
    private final BusinessService businessService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberDTO signUpInsertMemberInfo(SigunUpRequestDTO sigunUpRequestDTO) {

        if (sigunUpRequestDTO == null) {
            throw new BadRequestException(BadStatusCode.EMPTY_SIGNUP_INFO_EXCEPTION);
        }
        try {
            MemberDTO dto = MemberDTO.convertToMemberDTO(sigunUpRequestDTO);

            try {
                dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
            } catch (Exception e) {
                throw new ServerErrorException(BadStatusCode.PASSWORD_ENCODING_FAIL_EXCEPTION);
            }


            int i = memberMapper.insertMember(dto);
            if (i == 0) {
                throw new BadRequestException(BadStatusCode.FAIL_TO_REGISTER_MEMBER_EXCEPTION);
            }

            AuthDTO authDTO = new AuthDTO();
            authDTO.setMemberId(dto.getMemberId());
            authDTO.setAuth("ROLE_Member");
            int authResult = memberMapper.insertAuth(authDTO);
            if(authResult == 0 ){
                throw new ServerErrorException(BadStatusCode.FAIL_TO_REGISTER_MEMBER_AUTH_EXCEPTION);
            }

            return dto;

        } catch (DataAccessException e) {
            throw new ServerErrorException(BadStatusCode.DATABASE_PROCESSING_EXCEPTION);
        } catch (Exception e) {
            throw new ServerErrorException(BadStatusCode.INTERNAL_SERVER_EXCEPTION);
        }

    }

    @Transactional
    public void signup (SigunUpRequestDTO sigunUpRequestDTO){
        MemberDTO dto = signUpInsertMemberInfo(sigunUpRequestDTO);
        businessService.sigunUpInsertBusinessInfo(sigunUpRequestDTO, dto.getMemberId());

    }


}
