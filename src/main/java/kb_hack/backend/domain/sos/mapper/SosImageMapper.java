package kb_hack.backend.domain.sos.mapper;

import java.util.List;

import kb_hack.backend.domain.sos.entity.SosImage;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import kb_hack.backend.domain.sos.entity.SosImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SosImageMapper {
	int insert(SosImage image);
	@Select("SELECT storage_key FROM sos_image WHERE sos_id = #{sosId}")
	List<String> findImageKeysBySosId(Long sosId);

	@Select("SELECT * FROM sos_image WHERE sos_image_id = #{sosImageId}")
	SosImage findById(Long sosImageId);

	List<SosImage> findBySosId(Long sosId);

	void deleteBySosId(Long sosId);

	@Delete("DELETE FROM sos_image WHERE sos_image_id = #{sosImageId}")
	void deleteById(Long sosImageId);
}
