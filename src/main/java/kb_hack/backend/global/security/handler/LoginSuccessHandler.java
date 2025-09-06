package kb_hack.backend.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import kb_hack.backend.global.security.dto.SecurityMemberInfoDTO;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.dto.SecurityResponseDTO;
import kb_hack.backend.global.security.entity.MemberVO;
import kb_hack.backend.global.security.mapper.SecurityMemberMapper;
import kb_hack.backend.global.util.JwtProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProcessor jwtProcessor;
    private final ObjectMapper objectMapper;
    private final SecurityMemberMapper securityMemberMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityCustomUser user = (SecurityCustomUser) authentication.getPrincipal();
        MemberVO vo = user.getMemberVO();
        String email = vo.getMemberEmail();

        String accessToken = jwtProcessor.generateAccessToken(email);
        String refreshToken = jwtProcessor.generateRefreshToken(email);

        SecurityMemberInfoDTO dto = SecurityMemberInfoDTO.convertToDTO(vo);
        dto.setMinorNm(securityMemberMapper.getMinorNmByBusinessId(dto.getBusinessDTO().getBusinessId()));
        SecurityResponseDTO result = new SecurityResponseDTO(accessToken, refreshToken, dto);

        SuccessResponse<SecurityResponseDTO> body =
                SuccessResponse.makeResponse(SuccessStatusCode.LOGIN_SUCCESS, result);

        // HTTP 응답 작성
        response.setStatus(SuccessStatusCode.LOGIN_SUCCESS.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), body);


    }
}
