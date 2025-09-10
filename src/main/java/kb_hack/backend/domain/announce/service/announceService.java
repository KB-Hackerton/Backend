package kb_hack.backend.domain.announce.service;

import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.announce.dto.announceDetailDto;
import kb_hack.backend.domain.announce.dto.announceDto;
import kb_hack.backend.domain.announce.mapper.AnnounceMapper;
import kb_hack.backend.domain.document.dto.DocumentItemDto;
import kb_hack.backend.domain.document.dto.DocumentResponseDto;
import kb_hack.backend.domain.document.mapper.DocumentMapper;
import kb_hack.backend.domain.favorite.dto.FavoriteResponseDto;
import kb_hack.backend.domain.favorite.mapper.FavoriteMapper;
import kb_hack.backend.domain.favorite.service.FavoriteService;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class announceService {
    private final AnnounceMapper announceMapper;
    private final FavoriteService favoriteService;
    private final RecentAnnounceService recentAnnounceService;
    private final DocumentMapper documentMapper;

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

        // 로그인한 사용자 ID
        Long memberId = getLoginMemberId();

        // 제출서류 조회
        List<DocumentResponseDto> docs =
            documentMapper.findDocumentsByMemberAndAnnounce(memberId, announceId);

        List<DocumentItemDto> checklist = docs.stream()
            .map(d -> DocumentItemDto.builder()
                .documentId(d.getDocumentId())
                .title(d.getTitle())
                .description(d.getDescription())
                .checked(d.isChecked())
                .build())
            .toList();

        // DTO 변환 + 체크리스트 세팅
        announceDetailDto dto = announceDetailDto.from(announce);
        dto.setChecklist(checklist);

        return dto;
    }

    /** 🔑 현재 로그인한 사용자 ID 가져오기 */
    private Long getLoginMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityCustomUser securityUser = (SecurityCustomUser) authentication.getPrincipal();
        MemberVO vo = securityUser.getMemberVO();
        return vo.getMemberId();
    }
}