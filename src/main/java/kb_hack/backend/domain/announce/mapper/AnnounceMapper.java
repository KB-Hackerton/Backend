package kb_hack.backend.domain.announce.mapper;

import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.announce.dto.AnnounceRankingDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AnnounceMapper {
    void insertAnnounce(Announce announce);
    List<Announce> findAll();

    Announce findById(@Param("announceId") Long announceId);

    Optional<Announce> findById2(@Param("id") Long id);

    List<AnnounceRankingDTO> findTopAnnounces(@Param("limit") int limit);

    AnnounceRankingDTO findByAnnounceId(Long announceId);
    int increaseViewNum(@Param("announceId") Long announceId);
}
