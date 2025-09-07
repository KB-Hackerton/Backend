package kb_hack.backend.domain.kakao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.kakao.dto.request.KakaoCodeRequestDTO;
import kb_hack.backend.domain.kakao.dto.response.KakaoLoginResponse;
import kb_hack.backend.domain.kakao.dto.response.KakaoMemberInfoDTO;
import kb_hack.backend.domain.kakao.dto.response.KakaoTokenResponseDTO;
import kb_hack.backend.domain.kakao.service.KakaoService;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.ServerErrorException;
import kb_hack.backend.global.security.dto.SecurityResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kakao-login")
@Tag(name = "카카오 로그인 API", description = "카카오 OAuth 로그인 및 신규 사용자 여부 확인")
public class KakaoController {
    private final KakaoService kakaoService;

    @Operation(
            summary = "카카오 로그인",
            description = """
                      카카오 인가 코드(code)를 받아서 로그인 처리합니다.<br>
                      - **기존 사용자**: JWT AccessToken/RefreshToken 과 사용자 정보 반환<br>
                      - **신규 사용자**: flag=NEW_USER 와 카카오 ID, 이메일 반환
                      """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공 (기존 사용자)",
                    content = @Content(schema = @Schema(implementation = SecurityResponseDTO.class))),
            @ApiResponse(responseCode = "200", description = "신규 사용자 (회원가입 필요)",
                    content = @Content(schema = @Schema(implementation = KakaoLoginResponse.class))),
            @ApiResponse(responseCode = "500", description = "카카오 OAuth 처리 실패",
                    content = @Content(schema = @Schema(implementation = ServerErrorException.class)))
    })
    @PostMapping("")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoCodeRequestDTO kakaoCodeRequestDTO) {
        Object response = kakaoService.doKakaoLogin(kakaoCodeRequestDTO);

        if (response instanceof SecurityResponseDTO securityResponse) {
            return ResponseEntity.ok(securityResponse);
        } else if (response instanceof KakaoLoginResponse kakaoLoginResponse) {
            return ResponseEntity.status(HttpStatus.OK).body(kakaoLoginResponse);
        } else {
            throw new ServerErrorException(BadStatusCode.FAIL_TO_PROCESSING_KAKAO_OAUTH_EXCEPTION);
        }
    }
}


