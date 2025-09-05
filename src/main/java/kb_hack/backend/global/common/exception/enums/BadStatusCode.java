package kb_hack.backend.global.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BadStatusCode {
    //400 BAD REQUEST
    VERIFICATION_FAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),
    INVALID_ACCESS_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST,"토큰이 유효하지 않습니다."),
    INVALID_AUTHORIZATION_HEADER_EXCEPTION(HttpStatus.BAD_REQUEST,"인증 헤더가 유효하지 않습니다"),
    INVALID_PARAMETER_EXCEPTION(HttpStatus.BAD_REQUEST,"파라미터를 잘못 입력 하셨습니다."),
    EMPTY_SIGNUP_INFO_EXCEPTION(HttpStatus.BAD_REQUEST,"회원가입 요청 정보가 비었습니다."),
    INVALID_MINOR_NAME_EXCEPTION(HttpStatus.BAD_REQUEST,"잘못된 업종 이름 입니다."),
    FAIL_TO_REGISTER_BUSINESS_EXCEPTION(HttpStatus.BAD_REQUEST,"사업체 등록에 실패했습니다. 요청 데이터를 확인해주세요."),
    FAIL_TO_REGISTER_MEMBER_EXCEPTION(HttpStatus.BAD_REQUEST, "회원 등록에 실패했습니다. 요청 데이터를 확인해주세요."),
    INSUFFICIENT_EMAIL_VERIFICATION_CODE(HttpStatus.BAD_REQUEST,"인증번호가 존재하지 않거나 만료되었습니다."),
    INVAID_EMAIL_EXCEPTION(HttpStatus.BAD_REQUEST,"유효하지 않은 이메일 입니다."),


    //401 UNAUTHORIZED
    TOKEN_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    LOGIN_FAILURE_EXCEPTION(HttpStatus.UNAUTHORIZED,"아이디 혹은 비밀번호를 잘못 입력 했습니다."),
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED,"토큰의 서명이 유효하지 않습니다."),


    // 403 Forbidden (주로 인가)
    INSUFFICIENT_PERMISSION_EXCEPTION(HttpStatus.FORBIDDEN,"접근 권한이 없습니다."),

    // 404 Not Found
    ANNOUNCE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 공고번호 입니다."),
    USER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),


    // 5xx(Server Error)
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    DATABASE_PROCESSING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "DB 처리 중 오류가 발생했습니다."),
    PASSWORD_ENCODING_FAIL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "비밀번호 암호화 처리 중 오류가 발생했습니다."),
    FAIL_TO_REGISTER_MEMBER_AUTH_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "회원 권한 등록에 실패했습니다."),
    FAIL_TO_SAVE_VERIFICATION_CODE_REDIS_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"Redis에 인증번호 저장을 실패 했습니다."),
    FAIL_TO_HANDLE_VERIFICATION_CODE_REDIS_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"Redis에서 인증번호 조회/삭제 중 오류 발생 했습니다."),
    FAIL_TO_SEND_MAIL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"메일 서버 오류로 인증 메일 전송을 실패 했습니다."),


    // 크롤링 관련 5xx
    CRAWL_URL_FAIL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "크롤링 URL 관련 오류"),
    CRAW_IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "크롤링 IO 오류"),
    CRAWL_FAIL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "공고 크롤링 실패"),

    ANNOUNCE_DETAIL_GET_FAIL(HttpStatus.NOT_FOUND,"상세 공고 불러오기 실패");
    private final HttpStatus httpStatus;
    private final String message;
}
