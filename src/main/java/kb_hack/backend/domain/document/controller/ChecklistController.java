package kb_hack.backend.domain.document.controller;

import kb_hack.backend.domain.document.dto.DocumentCheckRequestDto;
import kb_hack.backend.domain.document.dto.DocumentResponseDto;
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

	/** 즐겨찾기 공고에서 PDF 제출서류 추출 & 저장 */
	@PostMapping("/extract")
	public ResponseEntity<String> extractFromFavorites() {
		Long memberId = 1L; // 고정
		int savedCount = documentService.extractAndSaveDocumentsFromFavorites(memberId);
		return ResponseEntity.ok(savedCount + "개의 제출서류가 저장되었습니다.");
	}

	/** 제출서류 체크리스트 조회 */
	@GetMapping
	public ResponseEntity<List<DocumentResponseDto>> getChecklist() {
		Long memberId = 1L; // 고정
		return ResponseEntity.ok(checklistService.getChecklist(memberId));
	}

	/** 체크/해제 업데이트 */
	@PutMapping("/{documentId}/check")
	public ResponseEntity<String> updateCheckStatus(
		@PathVariable Long documentId,
		@RequestBody DocumentCheckRequestDto dto) {
		Long memberId = 1L; // 고정
		checklistService.updateCheckStatus(memberId, documentId, dto);
		return ResponseEntity.ok("체크 상태가 업데이트되었습니다.");
	}
}
