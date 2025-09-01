package kb_hack.backend.global.common.exception.type;


import kb_hack.backend.global.common.exception.enums.BadStatusCode;

public class BadRequestException extends CustomException {
    public BadRequestException(BadStatusCode badStatusCode) {
        super(badStatusCode);
    }
}
