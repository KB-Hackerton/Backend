package kb_hack.backend.global.common.exception.type;


import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import org.flywaydb.core.api.ErrorCode;

public class BadRequestException extends RuntimeException {
    private final BadStatusCode badStatusCode;

    public BadRequestException(BadStatusCode badStatusCode) {
        super(badStatusCode.getMessage());
        this.badStatusCode = badStatusCode;
    }

    public BadStatusCode getBadStatusCode() {
        return badStatusCode;
    }
}
