package kb_hack.backend.global.security.filter;

import kb_hack.backend.global.security.handler.LoginFailureHandler;
import kb_hack.backend.global.security.handler.LoginSuccessHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, LoginSuccessHandler loginSuccessHandler, LoginFailureHandler loginFailureHandler){
        super(authenticationManager);
        this.setFilterProcessesUrl("/auth/login");
        this.setAuthenticationSuccessHandler(loginSuccessHandler);
        this.setAuthenticationFailureHandler(loginFailureHandler);

    }
}
