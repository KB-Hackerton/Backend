package kb_hack.backend.global.security.service;

import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.NotFoundException;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import kb_hack.backend.global.security.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityCustomUserDetailsService implements UserDetailsService {
    private final MemberMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);
        MemberVO vo = mapper.getMemberByMemberEmail(username);
        System.out.println("vo = " + vo);
        if(vo == null){
            throw new NotFoundException(BadStatusCode.USER_NOT_FOUND_EXCEPTION);
        }
        return new SecurityCustomUser(vo);
    }
}
