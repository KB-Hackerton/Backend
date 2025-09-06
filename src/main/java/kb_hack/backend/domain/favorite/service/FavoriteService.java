package kb_hack.backend.domain.favorite.service;

import java.util.List;

import kb_hack.backend.domain.favorite.dto.FavoriteRequestDto;
import kb_hack.backend.domain.favorite.dto.FavoriteResponseDto;
import kb_hack.backend.domain.favorite.mapper.FavoriteMapper;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final FavoriteMapper favoriteMapper;

	@Transactional
	public void addFavorite(FavoriteRequestDto dto) {
		Long memberId = getLoginMemberId(); // 로그인한 회원 ID 가져오기
		favoriteMapper.insertFavorite(dto.getAnnounceId(), memberId);
	}

	@Transactional
	public void removeFavorite(Long announceId) {
		Long memberId = getLoginMemberId();
		favoriteMapper.deleteFavorite(announceId, memberId);
	}

	@Transactional(readOnly = true)
	public List<FavoriteResponseDto> getFavorites() {
		Long memberId = getLoginMemberId();
		return favoriteMapper.findFavoritesByMemberId(memberId);
	}

	// 🔑 현재 로그인한 MemberId 조회
	private Long getLoginMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SecurityCustomUser securityUser = (SecurityCustomUser) authentication.getPrincipal();
		MemberVO vo = securityUser.getMemberVO();
		return vo.getMemberId();
	}
}
