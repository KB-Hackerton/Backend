package kb_hack.backend.global.common.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import kb_hack.backend.global.common.exception.type.BadRequestException;
import kb_hack.backend.global.common.response.bad.BadResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    //400 에러 핸들러
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadResponse> handle400Error(BadRequestException be,HttpServletRequest request){
        log.error("""
            ┌─ 400 Bad Request
            │ type    : {}
            │ code    : {}
            │ method  : {}
            │ url     : {}
            │ message : {}
            └─ stack  : below""",
                be.getClass().getSimpleName(),
                HttpStatus.BAD_REQUEST.value(),
                request.getMethod(),
                request.getRequestURI(),
                be.getMessage()
        );
        log.error("Stacktrace:", be);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BadResponse.makeResponse(be.getBadStatusCode()));
    }
}
