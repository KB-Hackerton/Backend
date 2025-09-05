package kb_hack.backend.global.common.exception.enums;

import kb_hack.backend.domain.festival.Festival;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatusCode {
    //200 OK
    LOGIN_SUCCESS(HttpStatus.OK,"로그인 성공!"),
    EMAIL_SEND_SUCCESS(HttpStatus.OK,"이메일 발송 성공!"),
    SIGNUP_SUCCESS(HttpStatus.CREATED,"회원 가입 성공!"),
    CRAWL_ARTICLE_SUCCESS(HttpStatus.OK,"공고 크롤링 성공!"),
    HEALTH_CHECK_SUCCESS(HttpStatus.OK,"서버가 정상 작동 중입니다!"),
    EMAIL_VERIFY_CODE_SUCCESS(HttpStatus.OK,"인증 코드 검증 성공!"),
    ANNOUNCE_GET_SUCCESS(HttpStatus.OK,"공고 리스트 불러오기 성공!"),
    FESTIVAL_GET_SUCCESS(HttpStatus.OK,"축제 리스트 불러오기 성공!"),
    SIGNOUT_SUCCESS(HttpStatus.OK,"회원 탈퇴 성공!");
    private final HttpStatus httpStatus;
    private final String message;

}
