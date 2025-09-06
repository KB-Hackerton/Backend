package kb_hack.backend.domain.home.service;

import kb_hack.backend.domain.announce.dto.RecentViewAnnounceDTO;
import kb_hack.backend.domain.announce.service.RecentAnnounceService;
import kb_hack.backend.domain.home.dto.request.RecentAnnounceDTO;
import kb_hack.backend.domain.home.dto.request.RecentArticleDTO;
import kb_hack.backend.domain.home.dto.request.RecentFestivalDTO;
import kb_hack.backend.domain.home.dto.response.HomeResponse;
import kb_hack.backend.domain.home.mapper.HomeMapper;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.ServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final HomeMapper homeMapper;
    private final RecentAnnounceService recentAnnounceService;

    @Transactional
    public HomeResponse getHomeData(){
        List<RecentAnnounceDTO> recentAnnounce;
        List<RecentFestivalDTO> recentFestival;
        List<RecentArticleDTO> recentArticle;

        try {
            recentAnnounce = homeMapper.getRecentAnnounce();
        } catch (Exception e) {
            throw new ServerErrorException(BadStatusCode.FAIL_TO_GET_RECENT_ANNOUNCE_EXCEPTION);
        }

        try {
            recentFestival = homeMapper.getRecentFestival();
        } catch (Exception e) {
            throw new ServerErrorException(BadStatusCode.FAIL_TO_GET_RECENT_FESTIVAL_EXCEPTION);
        }

        try {
            recentArticle = homeMapper.getRecentArticle();
        } catch (Exception e) {
            throw new ServerErrorException(BadStatusCode.FAIL_TO_GET_RECENT_ARTICLE_EXCEPTION);
        }

        List<RecentViewAnnounceDTO> recentAnnounceList = recentAnnounceService.getRecentAnnounceList();
        return new HomeResponse(recentAnnounce,recentFestival,recentArticle,recentAnnounceList);
    }
}
