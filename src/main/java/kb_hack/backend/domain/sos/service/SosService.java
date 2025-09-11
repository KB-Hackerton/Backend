package kb_hack.backend.domain.sos.service;

import java.util.List;

import kb_hack.backend.domain.sos.dto.SosCreateRequest;
import kb_hack.backend.domain.sos.dto.SosCreateResponse;
import kb_hack.backend.domain.sos.dto.SosDetailResponse;
import kb_hack.backend.domain.sos.dto.SosListResponse;
import kb_hack.backend.domain.sos.dto.SosUpdateRequest;

public interface SosService {
	SosCreateResponse create(SosCreateRequest req);
	void update(Long sosId, SosUpdateRequest req);

	void hardDelete(Long sosId);

	List<SosListResponse> getSosList();
	SosDetailResponse getSosDetail(Long sosId);
}
