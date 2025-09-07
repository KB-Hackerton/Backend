package kb_hack.backend.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.BadRequestException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProcessor {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_VALID_MILLISECOND = 1000L * 60 * 60 * 24 * 365 * 100L;
    private static final long REFRESH_TOKEN_VALID_MILLISECOND=1000L*60*60*24*7;
    private String secretKey = "KB_IT'S_YOUR_LIFE_HACKERTON_FIGHTING";
    private Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    public String generateAccessToken(String subject) {
        return makeJwtToken(subject,ACCESS_TOKEN_VALID_MILLISECOND);
    }

    public String generateRefreshToken(String subject){
        return makeJwtToken(subject,REFRESH_TOKEN_VALID_MILLISECOND);
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)          // (1) 서명 검증할 키 지정
                .build()                     // (2) 파서 생성
                .parseClaimsJws(token)       // (3) 토큰 해석 + 서명 검증
                .getBody()                   // (4) payload(JSON) 꺼냄
                .getSubject();               // (5) "sub" 클레임 값을 꺼냄
    }


    private String makeJwtToken(String subject,Long expiretime) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiretime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return true;
    }

    public String getUserEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)          // (1) 서명 검증할 키 지정
                .build()                     // (2) 파서 생성
                .parseClaimsJws(token)       // (3) 토큰 해석 + 서명 검증
                .getBody()                   // (4) payload(JSON) 꺼냄
                .getSubject();               // (5) "sub" 클레임 값을 꺼냄
    }



}
