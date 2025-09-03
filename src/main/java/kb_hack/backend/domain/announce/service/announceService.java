package kb_hack.backend.domain.announce.service;

import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.announce.dto.announceDto;
import kb_hack.backend.domain.announce.mapper.AnnounceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class announceService {
    private final AnnounceMapper announceMapper;

    public List<announceDto> getAnnounceList(){
        List<Announce> announces= announceMapper.findAll();
        //announce 리스트 => dto 리스트 변경
        return announces.stream()
                .map(announceDto::from)
                .collect(Collectors.toList());
    }
}
