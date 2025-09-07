package kb_hack.backend.domain.kakao.controller;

import kb_hack.backend.domain.kakao.dto.request.KakaoCodeRequestDTO;
import kb_hack.backend.domain.kakao.dto.response.KakaoLoginResponse;
import kb_hack.backend.domain.kakao.dto.response.KakaoMemberInfoDTO;
import kb_hack.backend.domain.kakao.dto.response.KakaoTokenResponseDTO;
import kb_hack.backend.domain.kakao.service.KakaoService;
import kb_hack.backend.global.common.response.success.SuccessResponse;
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
    public ResponseEntity<KakaoLoginResponse> testAccessToken(@RequestBody KakaoCodeRequestDTO kakaoCodeRequestDTO){
        KakaoLoginResponse kakaoLoginResponse = kakaoService.doKakaoLogin(kakaoCodeRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(kakaoLoginResponse);

    }


}
