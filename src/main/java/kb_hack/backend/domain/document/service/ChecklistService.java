package kb_hack.backend.domain.document.service;

import kb_hack.backend.domain.document.dto.ChecklistResponseDto;
import kb_hack.backend.domain.document.dto.DocumentCheckItemDto;
import kb_hack.backend.domain.document.dto.DocumentCheckRequestDto;
import kb_hack.backend.domain.document.dto.DocumentCheckBulkRequestDto;
import kb_hack.backend.domain.document.dto.DocumentItemDto;
import kb_hack.backend.domain.document.dto.DocumentResponseDto;
import kb_hack.backend.domain.document.mapper.DocumentMapper;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChecklistService {

	private final DocumentMapper documentMapper;

	/** 공고별 체크리스트 */
	@Transactional(readOnly = true)
	public ChecklistResponseDto getChecklistByAnnounce(Long announceId) {
		Long memberId = getLoginMemberId();
		List<DocumentResponseDto> docs = documentMapper.findDocumentsByMemberAndAnnounce(memberId, announceId);

		if (docs.isEmpty()) {
			return null; // or throw custom exception
		}

		// 공고 정보는 첫 번째 항목에서 꺼냄
		Long announceIdVal = docs.get(0).getAnnounceId();
		String announceTitle = docs.get(0).getAnnounceTitle();

		List<DocumentItemDto> checklist = docs.stream()
			.map(d -> DocumentItemDto.builder()
				.documentId(d.getDocumentId())
				.title(d.getTitle())
				.description(d.getDescription())
				.checked(d.isChecked())
				.build())
			.toList();

		return ChecklistResponseDto.builder()
			.announceId(announceIdVal)
			.announceTitle(announceTitle)
			.checklist(checklist)
			.build();
	}

	/** 회원 전체 즐겨찾기 체크리스트 */
	@Transactional(readOnly = true)
	public List<DocumentResponseDto> getChecklist() {
		Long memberId = getLoginMemberId();
		return documentMapper.findDocumentsByMemberId(memberId);
	}


	/** 단건 체크/해제 */
	@Transactional
	public void updateCheckStatus(Long documentId, DocumentCheckRequestDto dto) {
		Long memberId = getLoginMemberId();
		documentMapper.upsertDocumentCheck(documentId, memberId, dto.isChecked());
	}

	/** 공고별 배치 체크/해제 */
	@Transactional
	public void updateCheckStatusBulk(Long announceId, DocumentCheckBulkRequestDto bulkDto) {
		Long memberId = getLoginMemberId();
		if (bulkDto == null || bulkDto.getItems() == null) return;
		for (DocumentCheckItemDto item : bulkDto.getItems()) {
			if (item == null || item.getDocumentId() == null) continue;
			documentMapper.upsertDocumentCheck(item.getDocumentId(), memberId, item.isChecked());
		}
	}

	/** 🔑 현재 로그인한 memberId 가져오기 */
	private Long getLoginMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SecurityCustomUser securityUser = (SecurityCustomUser) authentication.getPrincipal();
		MemberVO vo = securityUser.getMemberVO();
		return vo.getMemberId();
	}
}