package kb_hack.backend.domain.home.mapper;

import kb_hack.backend.domain.home.dto.RecentAnnounceDTO;
import kb_hack.backend.domain.home.dto.RecentArticleDTO;
import kb_hack.backend.domain.home.dto.RecentFestivalDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HomeMapper {
     void getRecentAnnounce (RecentAnnounceDTO recentAnnounceDTO) ;
     void getRecentFestival (RecentFestivalDTO recentFestivalDTO) ;
     void getRecentArticle(RecentArticleDTO recentArticleDTO);
}
