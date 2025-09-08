package kb_hack.backend.domain.favorite.mapper;

import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.favorite.dto.FavoriteResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FavoriteMapper {
	void insertFavorite(@Param("announceId") Long announceId, @Param("memberId") Long memberId);
	void deleteFavorite(@Param("announceId") Long announceId, @Param("memberId") Long memberId);
	List<FavoriteResponseDto> findFavoritesByMemberId(@Param("memberId") Long memberId);
	List<Announce> findAnnouncesByMemberId(@Param("memberId") Long memberId);
}

