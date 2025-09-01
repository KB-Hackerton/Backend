package kb_hack.backend.global.common.exception.type;

import kb_hack.backend.global.common.exception.enums.BadStatusCode;

public class ServerErrorException extends CustomException {
    public ServerErrorException(BadStatusCode badStatusCode) {
        super(badStatusCode);
    }
}
