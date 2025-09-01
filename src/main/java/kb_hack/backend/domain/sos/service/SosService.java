package kb_hack.backend.domain.sos.service;

import kb_hack.backend.domain.sos.dto.SosCreateRequest;
import kb_hack.backend.domain.sos.dto.SosCreateResponse;

public interface SosService {
	SosCreateResponse create(SosCreateRequest req);
}
