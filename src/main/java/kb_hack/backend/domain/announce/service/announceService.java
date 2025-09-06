package kb_hack.backend.domain.announce.service;

import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.announce.dto.announceDetailDto;
import kb_hack.backend.domain.announce.dto.announceDto;
import kb_hack.backend.domain.announce.mapper.AnnounceMapper;
import kb_hack.backend.domain.favorite.dto.FavoriteResponseDto;
import kb_hack.backend.domain.favorite.mapper.FavoriteMapper;
import kb_hack.backend.domain.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class announceService {
    private final AnnounceMapper announceMapper;
    private final FavoriteService favoriteService;
    private final RecentAnnounceService recentAnnounceService;

    public List<announceDto> getAnnounceList(){
        List<Announce> announces= announceMapper.findAll();
        //사용자의 즐겨찾기 목록  id 가져오기
        List<FavoriteResponseDto> favorites =favoriteService.getFavorites();
        //[3,10,19]
        List<Long> favoriteIds = favorites.stream()
                .map(FavoriteResponseDto::getAnnounceId)
                .collect(Collectors.toList());
        //announce 리스트 => dto 리스트 변경
        return announces.stream()
                .map(announce -> announceDto.from(announce,favoriteIds.contains(announce.getAnnounceId())))
                .collect(Collectors.toList());
    }

    public announceDetailDto getAnnounceDetail(@Param("announceId") Long announceId) {
        Announce announce = announceMapper.findById(announceId);
        recentAnnounceService.addRecentAnnounce(String.valueOf(announceId),announce.getAnnounceTitle());
        if(announce==null){
            return null;
        }

        return announceDetailDto.from(announce);
    }
}
