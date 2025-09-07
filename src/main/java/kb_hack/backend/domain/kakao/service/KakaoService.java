package kb_hack.backend.domain.kakao.service;

import com.google.common.net.HttpHeaders;
import kb_hack.backend.domain.kakao.dto.request.KakaoCodeRequestDTO;
import kb_hack.backend.domain.kakao.dto.response.KakaoLoginResponse;
import kb_hack.backend.domain.kakao.dto.response.KakaoMemberInfoDTO;
import kb_hack.backend.domain.kakao.dto.response.KakaoTokenResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Log4j2
@RequiredArgsConstructor
public class KakaoService {
    @Qualifier("kakaoAuthWebClient")
    private final WebClient kakaoAuthWebClient;

    @Qualifier("kakaoApiWebClient")
    private final WebClient kakaoApiWebClient;

    @Value("${kakao.rest_key}")
    private String REST_API_KEY;

    @Value("${kakao.redirect_url}")
    private String REDIRECT_URL;

    public KakaoTokenResponseDTO getAccessToken(String authorizationCode) {
        KakaoTokenResponseDTO response = kakaoAuthWebClient.post()
                .uri("/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", REST_API_KEY)
                        .with("redirect_uri", REDIRECT_URL)
                        .with("code", authorizationCode))
                .retrieve()
                .bodyToMono(KakaoTokenResponseDTO.class)
                .block();
        return response;
    }


    public KakaoMemberInfoDTO getUserInfo(String accessToken) {
        KakaoMemberInfoDTO kakaoMemberInfoDTO = kakaoApiWebClient.post()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoMemberInfoDTO.class)
                .block();

        return kakaoMemberInfoDTO;
    }

    public KakaoLoginResponse doKakaoLogin(KakaoCodeRequestDTO kakaoCodeRequestDTO){
        KakaoTokenResponseDTO kakaoTokenResponseDTO = getAccessToken(kakaoCodeRequestDTO.getCode());
        KakaoMemberInfoDTO userInfo = getUserInfo(kakaoTokenResponseDTO.getAccessToken());

        return KakaoLoginResponse.builder()
                .kakaoId(userInfo.getId())
                .memberEmail(userInfo.getKakaoAccount().getEmail())
                .build();
    }





}
