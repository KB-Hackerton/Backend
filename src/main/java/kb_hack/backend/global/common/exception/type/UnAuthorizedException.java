package kb_hack.backend.global.common.exception.type;

import kb_hack.backend.global.common.exception.enums.BadStatusCode;

public class UnAuthorizedException extends CustomException {
    public UnAuthorizedException(BadStatusCode badStatusCode) {
        super(badStatusCode);
    }
}
