package kb_hack.backend.domain.festival.mapper;

import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.festival.Festival;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FestivalMapper {

    int batchUpsert(@Param("list") List<Festival> list);
    List<Festival> findAll();
    Festival findByFestivalId(@Param("festivalId") Long festivalId);
}
