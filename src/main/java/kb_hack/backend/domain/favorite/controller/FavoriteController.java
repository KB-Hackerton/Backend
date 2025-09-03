package kb_hack.backend.domain.favorite.controller;

import kb_hack.backend.domain.favorite.dto.FavoriteRequestDto;
import kb_hack.backend.domain.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

	private final FavoriteService favoriteService;

	@PostMapping
	public ResponseEntity<String> addFavorite(@RequestBody FavoriteRequestDto dto) {
		favoriteService.addFavorite(dto);
		return ResponseEntity.ok("즐겨찾기에 등록되었습니다. (memberId=1)");
	}
}

