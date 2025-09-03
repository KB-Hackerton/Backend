package kb_hack.backend.global.common.exception.enums;

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
    HEALTH_CHECK_SUCCESS(HttpStatus.OK,"서버가 정상 작동 중입니다!");

    private final HttpStatus httpStatus;
    private final String message;

}
