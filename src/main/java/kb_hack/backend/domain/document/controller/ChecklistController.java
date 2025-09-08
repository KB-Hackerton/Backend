package kb_hack.backend.domain.document.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.document.dto.*;
import kb_hack.backend.domain.document.service.ChecklistService;
import kb_hack.backend.domain.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name = "Checklist", description = "공고별 제출서류 체크리스트 API ")
@RequestMapping("/checklists")
@RequiredArgsConstructor
public class ChecklistController {

	private final DocumentService documentService;
	private final ChecklistService checklistService;

	@Operation(
			summary = "즐겨찾기된 모든 공고의 제출서류 추출 & 저장",
			description = "회원이 즐겨찾기한 모든 공고의 제출서류를 추출하여 DB에 저장합니다.",
			security = @SecurityRequirement(name = "bearerAuth") // 🔒 자물쇠
	)
	@PostMapping("/extract")
	public ResponseEntity<String> extractFromFavorites() {
		Long memberId = 1L;
		int savedCount = documentService.extractAndSaveDocumentsFromFavorites(memberId);
		return ResponseEntity.ok(savedCount + "개의 제출서류가 저장되었습니다.");
	}

	@Operation(
			summary = "공고별 제출서류 추출 & 저장",
			description = "특정 공고 ID의 제출서류를 추출하여 DB에 저장합니다.",
			security = @SecurityRequirement(name = "bearerAuth")
	)
	@PostMapping("/{announceId}/extract")
	public ResponseEntity<String> extractForAnnounce(@PathVariable Long announceId) {
		int savedCount = documentService.extractAndSaveDocumentsForAnnounce(announceId);
		return ResponseEntity.ok("[announceId=" + announceId + "] " + savedCount + "개의 제출서류가 저장되었습니다.");
	}

	@Operation(
			summary = "전체 체크리스트 조회",
			description = "회원이 즐겨찾기한 공고의 전체 제출서류 체크리스트를 조회합니다.",
			security = @SecurityRequirement(name = "bearerAuth")
	)
	@GetMapping
	public ResponseEntity<List<DocumentResponseDto>> getChecklist() {
		Long memberId = 1L;
		return ResponseEntity.ok(checklistService.getChecklist(memberId));
	}

	@Operation(
			summary = "공고별 체크리스트 조회",
			description = "특정 공고 ID의 제출서류 체크리스트를 조회합니다.",
			security = @SecurityRequirement(name = "bearerAuth")
	)
	@GetMapping("/{announceId}")
	public ResponseEntity<List<DocumentResponseDto>> getChecklistByAnnounce(@PathVariable Long announceId) {
		Long memberId = 1L;
		return ResponseEntity.ok(checklistService.getChecklistByAnnounce(memberId, announceId));
	}

	@Operation(
			summary = "단건 체크/해제",
			description = "특정 제출서류(documentId)의 체크 상태를 업데이트합니다.",
			security = @SecurityRequirement(name = "bearerAuth")
	)
	@PutMapping("/documents/{documentId}/check")
	public ResponseEntity<String> updateCheckStatus(
			@PathVariable Long documentId,
			@RequestBody DocumentCheckRequestDto dto) {
		Long memberId = 1L;
		checklistService.updateCheckStatus(memberId, documentId, dto);
		return ResponseEntity.ok("체크 상태가 업데이트되었습니다.");
	}

	@Operation(
			summary = "공고별 배치 체크/해제",
			description = "특정 공고 ID의 제출서류들을 일괄 체크/해제합니다.",
			security = @SecurityRequirement(name = "bearerAuth")
	)
	@PutMapping("/announces/{announceId}/check")
	public ResponseEntity<String> updateCheckStatusBulk(
			@PathVariable Long announceId,
			@RequestBody DocumentCheckBulkRequestDto bulkDto) {
		Long memberId = 1L;
		checklistService.updateCheckStatusBulk(memberId, announceId, bulkDto);
		return ResponseEntity.ok("체크 상태가 저장되었습니다. (announceId=" + announceId + ")");
	}

}
