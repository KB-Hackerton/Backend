package kb_hack.backend.domain.kakao.service;

import com.google.common.net.HttpHeaders;
import kb_hack.backend.domain.kakao.dto.request.KakaoCodeRequestDTO;
import kb_hack.backend.domain.kakao.dto.response.KakaoLoginResponse;
import kb_hack.backend.domain.kakao.dto.response.KakaoMemberInfoDTO;
import kb_hack.backend.domain.kakao.dto.response.KakaoTokenResponseDTO;
import kb_hack.backend.domain.kakao.mapper.KakaoMapper;

import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.ServerErrorException;
import kb_hack.backend.global.security.dto.SecurityMemberInfoDTO;
import kb_hack.backend.global.security.dto.SecurityResponseDTO;
import kb_hack.backend.global.security.entity.MemberVO;
import kb_hack.backend.global.security.mapper.SecurityMemberMapper;
import kb_hack.backend.global.util.JwtProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


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

    private final KakaoMapper kakaoMapper;
    private final JwtProcessor jwtProcessor;
    private final SecurityMemberMapper securityMemberMapper;

    public KakaoTokenResponseDTO getAccessToken(String authorizationCode) {
        try {
            KakaoTokenResponseDTO response = kakaoAuthWebClient.post()
                    .uri("/oauth/token")
                    .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                            .with("client_id", REST_API_KEY)
                            .with("redirect_uri", REDIRECT_URL)
                            .with("code", authorizationCode))
                    .retrieve()
                    .bodyToMono(KakaoTokenResponseDTO.class)
                    .block();

            if (response == null) {
                throw new ServerErrorException(BadStatusCode.KAKAO_TOKEN_EMPTY);
            }

            return response;

        } catch (Exception e) {
            throw new ServerErrorException(BadStatusCode.KAKAO_TOKEN_REQUEST_FAILED);
        }
    }



    public KakaoMemberInfoDTO getUserInfo(String accessToken) {
        try {
            KakaoMemberInfoDTO kakaoMemberInfoDTO = kakaoApiWebClient.post()
                    .uri("/v2/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(KakaoMemberInfoDTO.class)
                    .block();

            if (kakaoMemberInfoDTO == null) {
                throw new ServerErrorException(BadStatusCode.KAKAO_USERINFO_EMPTY);
            }

            return kakaoMemberInfoDTO;

        } catch (Exception e) {
            throw new ServerErrorException(BadStatusCode.KAKAO_USERINFO_REQUEST_FAILED);
        }
    }


    public Object doKakaoLogin(KakaoCodeRequestDTO kakaoCodeRequestDTO) {
        try {
            KakaoTokenResponseDTO kakaoTokenResponseDTO = getAccessToken(kakaoCodeRequestDTO.getCode());
            KakaoMemberInfoDTO userInfo = getUserInfo(kakaoTokenResponseDTO.getAccessToken());

            boolean exists = kakaoMapper.existsByKakaoId(userInfo.getId()) == 1;

            if (exists) {
                MemberVO vo = kakaoMapper.findByKakaoId(userInfo.getId());
                if (vo == null) {
                    throw new ServerErrorException(BadStatusCode.DATABASE_PROCESSING_EXCEPTION);
                }
                String email = vo.getMemberEmail();


                //jwt 생성
                String accessToken = jwtProcessor.generateAccessToken(email);
                String refreshToken = jwtProcessor.generateRefreshToken(email);


                SecurityMemberInfoDTO dto = SecurityMemberInfoDTO.convertToDTO(vo);
                dto.setMinorNm(securityMemberMapper.getMinorNmByBusinessId(dto.getBusinessDTO().getBusinessId()));


                SecurityResponseDTO result = new SecurityResponseDTO(accessToken, refreshToken, dto);
                return result;
            } else {
                return KakaoLoginResponse.builder()
                        .flag("NEW_USER")
                        .kakaoId(userInfo.getId())
                        .memberEmail(userInfo.getKakaoAccount().getEmail())
                        .build();
            }
        } catch (WebClientResponseException e) {
            throw new ServerErrorException(BadStatusCode.FAIL_TO_COMMUNICATE_KAKAO_OAUTH_EXCEPTION);
        } catch (Exception e) {
            throw new ServerErrorException(BadStatusCode.FAIL_TO_PROCESSING_KAKAO_OAUTH_EXCEPTION);
        }
    }
}
