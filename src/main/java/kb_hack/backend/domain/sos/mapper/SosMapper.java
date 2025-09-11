package kb_hack.backend.domain.sos.mapper;

import java.util.List;

import kb_hack.backend.domain.sos.dto.SosDetailRow;
import kb_hack.backend.domain.sos.dto.SosListResponse;
import kb_hack.backend.domain.sos.entity.Sos;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SosMapper {
	int insert(Sos sos);
	int update(Sos sos);
	int hardDelete(Long sosId);

	// SosMapper.java
	List<SosListResponse> findAll();
	List<SosDetailRow> findDetail(Long sosId);

	Sos findById(Long sosId);
	Sos findByMemberId(Long memberId);

	int updateIsComplete(Long sosId);
}
