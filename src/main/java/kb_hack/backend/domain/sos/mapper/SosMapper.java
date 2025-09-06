package kb_hack.backend.domain.sos.mapper;

import kb_hack.backend.domain.sos.entity.Sos;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Insert;


import kb_hack.backend.domain.sos.entity.Sos;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SosMapper {
	int insert(Sos sos);
	int update(Sos sos);

	int hardDelete(Long sosId);
}
