package kb_hack.backend.global.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kb_hack.backend.global.security.dto.LoginRequestDTO;
import kb_hack.backend.global.security.handler.LoginFailureHandler;
import kb_hack.backend.global.security.handler.LoginSuccessHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, LoginSuccessHandler loginSuccessHandler, LoginFailureHandler loginFailureHandler){
        super(authenticationManager);
        this.setFilterProcessesUrl("/auth/login");
        this.setAuthenticationSuccessHandler(loginSuccessHandler);
        this.setAuthenticationFailureHandler(loginFailureHandler);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request , HttpServletResponse response) throws AuthenticationException {
        LoginRequestDTO dto = LoginRequestDTO.convert(request);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getMemberEmail(), dto.getPassword());
        return authenticationManager.authenticate(token);
    }


}
