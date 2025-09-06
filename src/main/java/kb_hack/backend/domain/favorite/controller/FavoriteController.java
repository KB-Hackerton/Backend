package kb_hack.backend.domain.favorite.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.favorite.dto.FavoriteRequestDto;
import kb_hack.backend.domain.favorite.dto.FavoriteResponseDto;
import kb_hack.backend.domain.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Favorite", description = "즐겨찾기 API ")
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

	private final FavoriteService favoriteService;

	@Operation(
			summary = "즐겨찾기 추가",
			description = "공고를 즐겨찾기에 등록합니다. JWT 인증 필요.",
			security = @SecurityRequirement(name = "bearerAuth")
	)
	@PostMapping
	public ResponseEntity<String> addFavorite(@RequestBody FavoriteRequestDto dto) {
		favoriteService.addFavorite(dto);
		return ResponseEntity.ok("즐겨찾기에 등록되었습니다. (memberId=1)");
	}

	@Operation(
			summary = "즐겨찾기 삭제",
			description = "특정 공고를 즐겨찾기에서 제거합니다. JWT 인증 필요.",
			security = @SecurityRequirement(name = "bearerAuth")
	)
	@DeleteMapping("/{announceId}")
	public ResponseEntity<String> removeFavorite(@PathVariable Long announceId) {
		favoriteService.removeFavorite(announceId);
		return ResponseEntity.ok("즐겨찾기에서 삭제되었습니다. (memberId=1)");
	}

	@Operation(
			summary = "즐겨찾기 목록 조회",
			description = "내가 등록한 즐겨찾기 공고 목록을 조회합니다. JWT 인증 필요.",
			security = @SecurityRequirement(name = "bearerAuth")
	)
	@GetMapping
	public ResponseEntity<List<FavoriteResponseDto>> getFavorites() {
		return ResponseEntity.ok(favoriteService.getFavorites());
	}
}
