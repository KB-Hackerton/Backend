package kb_hack.backend.domain.sos.mapper;

import kb_hack.backend.domain.sos.entity.SosImage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface SosImageMapper {

	@Insert("""
        INSERT INTO sos_image (sos_id, storage_key, is_deleted)
        VALUES (#{sosId}, #{storageKey}, #{isDeleted})
    """)
	@Options(useGeneratedKeys = true, keyProperty = "sosImageId", keyColumn = "sos_image_id")
	int insert(SosImage image);
}
