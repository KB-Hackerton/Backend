package kb_hack.backend.global.common.enums;

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

    //201 CREATED
    SIGNUP_SUCCESS(HttpStatus.CREATED,"회원 가입 성공!");

    private final HttpStatus httpStatus;
    private final String message;

    //302 FOUND
}
