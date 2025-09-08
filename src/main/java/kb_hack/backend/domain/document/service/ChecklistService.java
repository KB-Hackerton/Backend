package kb_hack.backend.domain.document.service;

import kb_hack.backend.domain.document.dto.DocumentCheckItemDto;
import kb_hack.backend.domain.document.dto.DocumentCheckRequestDto;
import kb_hack.backend.domain.document.dto.DocumentCheckBulkRequestDto;
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

	/** 회원 전체 즐겨찾기 체크리스트 */
	@Transactional(readOnly = true)
	public List<DocumentResponseDto> getChecklist() {
		Long memberId = getLoginMemberId();
		return documentMapper.findDocumentsByMemberId(memberId);
	}

	/** 공고별 체크리스트 */
	@Transactional(readOnly = true)
	public List<DocumentResponseDto> getChecklistByAnnounce(Long announceId) {
		Long memberId = getLoginMemberId();
		return documentMapper.findDocumentsByMemberAndAnnounce(memberId, announceId);
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
