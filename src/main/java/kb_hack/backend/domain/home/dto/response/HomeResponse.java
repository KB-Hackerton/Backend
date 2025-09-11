package kb_hack.backend.domain.home.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import kb_hack.backend.domain.announce.dto.AnnounceRankingDTO;
import kb_hack.backend.domain.announce.dto.RecentViewAnnounceDTO;
import kb_hack.backend.domain.home.dto.request.RecentAnnounceDTO;
import kb_hack.backend.domain.home.dto.request.RecentArticleDTO;
import kb_hack.backend.domain.home.dto.request.RecentFestivalDTO;
import kb_hack.backend.domain.notice.dto.response.NoticeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HomeResponse {
    List<AnnounceRankingDTO> announceRanking;
    List<RecentAnnounceDTO> recentAnnounce;
    List<RecentFestivalDTO> recentFestival;

    List<RecentArticleDTO> recentArticle;
    List<RecentViewAnnounceDTO> recentViewAnnounce;
    List<NoticeDTO> notice;
}
