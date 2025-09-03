package kb_hack.backend.domain.announce.mapper;

import kb_hack.backend.domain.announce.Announce;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnnounceMapper {
    void insertAnnounce(Announce announce);
    List<Announce> findAll();
    Announce findById(@Param("announceId") Long announceId);
}
