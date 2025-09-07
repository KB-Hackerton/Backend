package kb_hack.backend.domain.kakao.controller;

import kb_hack.backend.domain.kakao.dto.request.KakaoCodeRequestDTO;
import kb_hack.backend.domain.kakao.dto.response.KakaoLoginResponse;
import kb_hack.backend.domain.kakao.dto.response.KakaoMemberInfoDTO;
import kb_hack.backend.domain.kakao.dto.response.KakaoTokenResponseDTO;
import kb_hack.backend.domain.kakao.service.KakaoService;
import kb_hack.backend.global.common.response.success.SuccessResponse;
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
public class KakaoController {
    private final KakaoService kakaoService;

    @PostMapping("")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoCodeRequestDTO kakaoCodeRequestDTO) {
        Object response = kakaoService.doKakaoLogin(kakaoCodeRequestDTO);

        if (response instanceof SecurityResponseDTO securityResponse) {
            // 기존 회원 → 로그인 성공
            return ResponseEntity.ok(securityResponse);
        } else if (response instanceof KakaoLoginResponse kakaoLoginResponse) {
            // 신규 회원 → 회원가입 필요
            return ResponseEntity.status(HttpStatus.OK).body(kakaoLoginResponse);
        } else {
            // 예외 상황
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected response type");
        }
    }
}


