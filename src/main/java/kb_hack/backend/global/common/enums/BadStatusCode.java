package kb_hack.backend.global.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BadStatusCode {
    //400 BAD REQUEST
    INVALID_PASSWORD_EXCEPTION(HttpStatus.BAD_REQUEST, "비밀번호가 잘못되었습니다."),

    //401 UNAUTHORIZED
    TOKEN_TIME_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    VERIFICATION_FAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),
    USER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
    ANNOUNCE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 공고번호 입니다.");


    // 403 Forbidden

    // 404 Not Found

    //405 METHOD_NOT_ALLOWED

    // 5xx(Server Error)



    private final HttpStatus httpStatus;
    private final String message;
}
