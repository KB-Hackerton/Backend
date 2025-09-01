package kb_hack.backend.global.common.exception.type;

import kb_hack.backend.global.common.exception.enums.BadStatusCode;

public class NotFoundException extends CustomException {
    public NotFoundException(BadStatusCode badStatusCode) {
        super(badStatusCode);
    }
}
