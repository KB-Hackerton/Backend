package kb_hack.backend.global.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kb_hack.backend.global.Discord.service.DiscordService;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.BadRequestException;
import kb_hack.backend.global.common.exception.type.ServerErrorException;
import kb_hack.backend.global.common.exception.type.UnAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationErrorFilter extends OncePerRequestFilter {

    @Autowired
    private DiscordService discordService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            String clientIp = extractClientIp(request);
            String requestInfo = request.getMethod() + " " + request.getRequestURI();
            discordService.send4xxNotification("토큰이 만료 되었습니다.", requestInfo, clientIp);
            throw new UnAuthorizedException(BadStatusCode.TOKEN_EXPIRED_EXCEPTION);

        } catch (io.jsonwebtoken.security.SignatureException | SecurityException e) {
            String clientIp = extractClientIp(request);
            String requestInfo = request.getMethod() + " " + request.getRequestURI();
            discordService.send4xxNotification("잘못된 서명 입니다.", requestInfo, clientIp);
            throw new BadRequestException(BadStatusCode.INVALID_TOKEN_SIGNATURE);

        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            String clientIp = extractClientIp(request);
            String requestInfo = request.getMethod() + " " + request.getRequestURI();
            discordService.send4xxNotification("지원되지 않거나 잘못된 토큰 입니다.", requestInfo, clientIp);
            throw new UnAuthorizedException(BadStatusCode.TOKEN_EXPIRED_EXCEPTION);

        } catch (ServletException e) {
            String clientIp = extractClientIp(request);
            String requestInfo = request.getMethod() + " " + request.getRequestURI();
            discordService.send5xxNotification(
                    "서블릿 내부 오류 입니다.",
                    e.getMessage(),
                    clientIp,
                    requestInfo
            );
            throw new ServerErrorException(BadStatusCode.INTERNAL_SERVER_EXCEPTION);
        }
    }

    private String extractClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp != null && !clientIp.isBlank()) {
            return clientIp.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
