package kb_hack.backend.domain.document.controller;

import kb_hack.backend.domain.document.dto.*;
import kb_hack.backend.domain.document.service.ChecklistService;
import kb_hack.backend.domain.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/checklists")
@RequiredArgsConstructor
public class ChecklistController {

	private final DocumentService documentService;
	private final ChecklistService checklistService;

	/** (기존) 즐겨찾기 전체에서 PDF 제출서류 추출 & 저장 */
	@PostMapping("/extract")
	public ResponseEntity<String> extractFromFavorites() {
		Long memberId = 1L;
		int savedCount = documentService.extractAndSaveDocumentsFromFavorites(memberId);
		return ResponseEntity.ok(savedCount + "개의 제출서류가 저장되었습니다.");
	}

	/** ✅ 공고별: 제출서류 추출 & 저장 */
	@PostMapping("/{announceId}/extract")
	public ResponseEntity<String> extractForAnnounce(@PathVariable Long announceId) {
		int savedCount = documentService.extractAndSaveDocumentsForAnnounce(announceId);
		return ResponseEntity.ok("[announceId=" + announceId + "] " + savedCount + "개의 제출서류가 저장되었습니다.");
	}

	/** (기존) 즐겨찾기 전체 체크리스트 조회 */
	@GetMapping
	public ResponseEntity<List<DocumentResponseDto>> getChecklist() {
		Long memberId = 1L;
		return ResponseEntity.ok(checklistService.getChecklist(memberId));
	}

	/** ✅ 공고별: 체크리스트 조회 */
	@GetMapping("/{announceId}")
	public ResponseEntity<List<DocumentResponseDto>> getChecklistByAnnounce(@PathVariable Long announceId) {
		Long memberId = 1L;
		return ResponseEntity.ok(checklistService.getChecklistByAnnounce(memberId, announceId));
	}



	// 단건 체크/해제
	@PutMapping("/documents/{documentId}/check")
	public ResponseEntity<String> updateCheckStatus(
		@PathVariable Long documentId,
		@RequestBody DocumentCheckRequestDto dto) {
		Long memberId = 1L;
		checklistService.updateCheckStatus(memberId, documentId, dto);
		return ResponseEntity.ok("체크 상태가 업데이트되었습니다.");
	}

	// 공고별 배치 체크/해제
	@PutMapping("/announces/{announceId}/check")
	public ResponseEntity<String> updateCheckStatusBulk(
		@PathVariable Long announceId,
		@RequestBody DocumentCheckBulkRequestDto bulkDto) {
		Long memberId = 1L;
		checklistService.updateCheckStatusBulk(memberId, announceId, bulkDto);
		return ResponseEntity.ok("체크 상태가 저장되었습니다. (announceId=" + announceId + ")");
	}

}
