package kb_hack.backend.global.common.exception.type;

import kb_hack.backend.global.common.exception.enums.BadStatusCode;

public class ForbiddenException extends RuntimeException {
    private final BadStatusCode badStatusCode;

    public ForbiddenException(BadStatusCode badStatusCode) {
        super(badStatusCode.getMessage());
        this.badStatusCode = badStatusCode;
    }

    public BadStatusCode getBadStatusCode() {
        return badStatusCode;
    }
}
