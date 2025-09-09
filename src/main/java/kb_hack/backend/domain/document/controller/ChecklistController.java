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
	@Operation(
		summary = "전체 공고 제출서류 추출 & 저장",
		description = "announce 테이블의 모든 공고를 대상으로 제출서류를 추출하여 DB에 저장합니다."
	)
	@PostMapping("/extract/all")
	public ResponseEntity<String> extractForAllAnnounces() {
		int savedCount = documentService.extractAndSaveDocumentsForAllAnnounces();
		return ResponseEntity.ok("전체 공고에서 " + savedCount + "개의 제출서류가 저장되었습니다.");
	}

	// /** ✅ 즐겨찾기된 모든 공고 제출서류 추출 & 저장 */
	// @PostMapping("/extract")
	// public ResponseEntity<String> extractFromFavorites() {
	// 	Long memberId = getLoginMemberId();
	// 	int savedCount = documentService.extractAndSaveDocumentsFromFavorites(memberId);
	// 	return ResponseEntity.ok(savedCount + "개의 제출서류가 저장되었습니다.");
	// }
	@Operation(
		summary = "특정 공고 제출서류 추출 & 저장",
		description = "공고 ID(`announceId`)를 기반으로 해당 공고의 제출서류를 추출하여 DB에 저장합니다."
	)

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
	@Operation(
		summary = "공고별 체크리스트 조회",
		description = "특정 공고 ID(`announceId`)에 해당하는 제출서류 체크리스트를 조회합니다."
	)
	@GetMapping("/{announceId}")
	public ResponseEntity<ChecklistResponseDto> getChecklistByAnnounce(@PathVariable Long announceId) {
		return ResponseEntity.ok(checklistService.getChecklistByAnnounce(announceId));
	}

	// @Operation(
	// 	summary = "단건 체크/해제",
	// 	description = "제출서류 ID(`documentId`)에 대해 체크 상태를 ON/OFF로 변경합니다."
	// )
	// /** ✅ 단건 체크/해제 */
	// @PutMapping("/documents/{documentId}/check")
	// public ResponseEntity<String> updateCheckStatus(
	// 	@PathVariable Long documentId,
	// 	@RequestBody DocumentCheckRequestDto dto) {
	// 	checklistService.updateCheckStatus(documentId, dto);
	// 	return ResponseEntity.ok("체크 상태가 업데이트되었습니다.");
	// }
	@Operation(
		summary = "공고별 배치 체크/해제",
		description = "특정 공고 ID(`announceId`)에 대해 제출서류 전체의 체크 상태를 일괄 저장합니다."
	)
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