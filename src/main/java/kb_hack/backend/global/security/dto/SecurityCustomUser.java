package kb_hack.backend.global.security.dto;

import kb_hack.backend.global.security.entity.MemberVO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class SecurityCustomUser extends User {
   private MemberVO memberVO;

    public SecurityCustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public SecurityCustomUser(MemberVO vo){
        super(vo.getMemberName(),vo.getPassword(),vo.getAuthMap());
        this.memberVO = vo;
    }
}
