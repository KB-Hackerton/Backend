package kb_hack.backend.global.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BadStatusCode {
    //400 BAD REQUEST
    INVALID_PASSWORD_EXCEPTION(HttpStatus.BAD_REQUEST, "비밀번호가 잘못되었습니다."),
    VERIFICATION_FAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),

    //401 UNAUTHORIZED
    TOKEN_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),

    // 403 Forbidden (주로 인가)
    INSUFFICIENT_PERMISSION_EXCEPTION(HttpStatus.FORBIDDEN,"접근 권한이 없습니다."),

    // 404 Not Found
    ANNOUNCE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 공고번호 입니다."),
    USER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),

    // 5xx(Server Error)
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    // 크롤링 관련 5xx
    CRAWL_URL_FAIL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "크롤링 URL 관련 오류"),
    CRAW_IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "크롤링 IO 오류"),
    CRAWL_FAIL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "공고 크롤링 실패");


    private final HttpStatus httpStatus;
    private final String message;
}
