package kb_hack.backend.domain.sos.mapper;

import kb_hack.backend.domain.sos.entity.Sos;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Insert;

@Mapper
public interface SosMapper {

	@Insert("""
        INSERT INTO sos (member_id, sos_title, sos_type, sos_content, expires_at, is_complete, is_deleted)
        VALUES (#{memberId}, #{sosTitle}, #{sosType}, #{sosContent}, #{expiresAt}, #{isComplete}, #{isDeleted})
    """)
	@Options(useGeneratedKeys = true, keyProperty = "sosId", keyColumn = "sos_id")
	int insert(Sos sos);
}
