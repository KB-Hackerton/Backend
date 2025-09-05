package kb_hack.backend.domain.email.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailMapper {
    int findMemberIDByEmail(String email);
}
