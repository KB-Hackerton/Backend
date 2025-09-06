package kb_hack.backend.domain.sos.mapper;

import kb_hack.backend.domain.sos.entity.SosImage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import kb_hack.backend.domain.sos.entity.SosImage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SosImageMapper {
	int insert(SosImage image);
}
