package kb_hack.backend.domain.favorite.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FavoriteMapper {
	void insertFavorite(@Param("announceId") Long announceId, @Param("memberId") Long memberId);
}

