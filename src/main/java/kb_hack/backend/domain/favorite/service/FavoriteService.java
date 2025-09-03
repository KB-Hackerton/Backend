package kb_hack.backend.domain.favorite.service;

import java.util.List;

import kb_hack.backend.domain.favorite.dto.FavoriteRequestDto;
import kb_hack.backend.domain.favorite.dto.FavoriteResponseDto;
import kb_hack.backend.domain.favorite.mapper.FavoriteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final FavoriteMapper favoriteMapper;

	@Transactional
	public void addFavorite(FavoriteRequestDto dto) {
		Long memberId = 1L; // 고정
		favoriteMapper.insertFavorite(dto.getAnnounceId(), memberId);
	}

	@Transactional
	public void removeFavorite(Long announceId) {
		Long memberId = 1L; // 고정
		favoriteMapper.deleteFavorite(announceId, memberId);
	}

	@Transactional(readOnly = true)
	public List<FavoriteResponseDto> getFavorites() {
		Long memberId = 1L; // 고정
		return favoriteMapper.findFavoritesByMemberId(memberId);
	}
}
