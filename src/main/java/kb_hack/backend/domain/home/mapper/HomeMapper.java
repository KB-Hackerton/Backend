package kb_hack.backend.domain.home.mapper;

import kb_hack.backend.domain.home.dto.request.RecentAnnounceDTO;
import kb_hack.backend.domain.home.dto.request.RecentArticleDTO;
import kb_hack.backend.domain.home.dto.request.RecentFestivalDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HomeMapper {
     List<RecentAnnounceDTO> getRecentAnnounce();
     List<RecentFestivalDTO> getRecentFestival();
     List<RecentArticleDTO> getRecentArticle();

}
