package kb_hack.backend.domain.home.service;

import kb_hack.backend.domain.home.dto.RecentAnnounceDTO;
import kb_hack.backend.domain.home.dto.RecentArticleDTO;
import kb_hack.backend.domain.home.dto.RecentFestivalDTO;
import kb_hack.backend.domain.home.mapper.HomeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final HomeMapper homeMapper;

    @Transactional
    public void getHomeData(){
        RecentAnnounceDTO recentAnnounceDTO = new RecentAnnounceDTO();
        RecentArticleDTO recentArticleDTO = new RecentArticleDTO();
        RecentFestivalDTO recentFestivalDTO = new RecentFestivalDTO();

    }
}
