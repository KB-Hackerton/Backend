package kb_hack.backend.domain.document.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.document.dto.*;
import kb_hack.backend.domain.document.service.ChecklistService;
import kb_hack.backend.domain.document.service.DocumentService;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name = "Checklist", description = "공고별 제출서류 체크리스트 API ")
@RequestMapping("/checklists")
@RequiredArgsConstructor
public class ChecklistController {

	private final DocumentService documentService;
	private final ChecklistService checklistService;

	// /** ✅ 즐겨찾기된 모든 공고 제출서류 추출 & 저장 */
	// @PostMapping("/extract")
	// public ResponseEntity<String> extractFromFavorites() {
	// 	Long memberId = getLoginMemberId();
	// 	int savedCount = documentService.extractAndSaveDocumentsFromFavorites(memberId);
	// 	return ResponseEntity.ok(savedCount + "개의 제출서류가 저장되었습니다.");
	// }

	/** ✅ 특정 공고 제출서류 추출 & 저장 */
	@PostMapping("/{announceId}/extract")
	public ResponseEntity<String> extractForAnnounce(@PathVariable Long announceId) {
		int savedCount = documentService.extractAndSaveDocumentsForAnnounce(announceId);
		return ResponseEntity.ok("[announceId=" + announceId + "] " + savedCount + "개의 제출서류가 저장되었습니다.");
	}

	// /** ✅ 전체 체크리스트 조회 */
	// @GetMapping
	// public ResponseEntity<List<DocumentResponseDto>> getChecklist() {
	// 	return ResponseEntity.ok(checklistService.getChecklist());
	// }

	/** ✅ 공고별 체크리스트 조회 */
	@GetMapping("/{announceId}")
	public ResponseEntity<List<DocumentResponseDto>> getChecklistByAnnounce(@PathVariable Long announceId) {
		return ResponseEntity.ok(checklistService.getChecklistByAnnounce(announceId));
	}

	/** ✅ 단건 체크/해제 */
	@PutMapping("/documents/{documentId}/check")
	public ResponseEntity<String> updateCheckStatus(
		@PathVariable Long documentId,
		@RequestBody DocumentCheckRequestDto dto) {
		checklistService.updateCheckStatus(documentId, dto);
		return ResponseEntity.ok("체크 상태가 업데이트되었습니다.");
	}

	/** ✅ 공고별 배치 체크/해제 */
	@PutMapping("/announces/{announceId}/check")
	public ResponseEntity<String> updateCheckStatusBulk(
		@PathVariable Long announceId,
		@RequestBody DocumentCheckBulkRequestDto bulkDto) {
		checklistService.updateCheckStatusBulk(announceId, bulkDto);
		return ResponseEntity.ok("체크 상태가 저장되었습니다. (announceId=" + announceId + ")");
	}

	/** 🔑 로그인한 사용자 ID 가져오기 */
	private Long getLoginMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SecurityCustomUser securityUser = (SecurityCustomUser) authentication.getPrincipal();
		MemberVO vo = securityUser.getMemberVO();
		return vo.getMemberId();
	}
}

