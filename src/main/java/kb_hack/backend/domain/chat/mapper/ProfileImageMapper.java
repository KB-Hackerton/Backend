package kb_hack.backend.domain.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProfileImageMapper {
	@Select("""
		SELECT profile_image.storage_key
		FROM profile_image
		WHERE member_id = #{memberId}
	""")
	String findProfileImageByMemberId(Long memberId);
}
