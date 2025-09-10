package kb_hack.backend.domain.sos.mapper;

import java.util.List;
import java.util.Map;

import kb_hack.backend.domain.sos.dto.SosDetailResponse;
import kb_hack.backend.domain.sos.dto.SosDetailRow;
import kb_hack.backend.domain.sos.dto.SosListResponse;
import kb_hack.backend.domain.sos.entity.Sos;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Insert;


import kb_hack.backend.domain.sos.entity.Sos;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
