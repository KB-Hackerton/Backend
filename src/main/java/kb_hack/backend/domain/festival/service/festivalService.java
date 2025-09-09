package kb_hack.backend.domain.festival.service;

import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.announce.dto.announceDetailDto;
import kb_hack.backend.domain.announce.dto.announceDto;
import kb_hack.backend.domain.announce.mapper.AnnounceMapper;
import kb_hack.backend.domain.festival.Festival;
import kb_hack.backend.domain.festival.dto.festivalDetailDto;
import kb_hack.backend.domain.festival.dto.festivalDto;
import kb_hack.backend.domain.festival.mapper.FestivalMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class festivalService {
    private final FestivalMapper festivalMapper;

    public List<festivalDto> getFestivalList(){
        List<Festival> festivals = festivalMapper.findAll();
        return festivals.stream()
                .map(festival -> festivalDto.from(festival))
                .collect(Collectors.toList());
    }

    public festivalDetailDto getFestivalDetail(@Param("festivalId") Long festivalId){
        Festival festival =festivalMapper.findByFestivalId(festivalId);
        return festivalDetailDto.from(festival);
    }

}
