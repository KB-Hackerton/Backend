package kb_hack.backend.domain.document.service;

import kb_hack.backend.domain.document.dto.DocumentCheckItemDto;
import kb_hack.backend.domain.document.dto.DocumentCheckRequestDto;
import kb_hack.backend.domain.document.dto.DocumentCheckBulkRequestDto;
import kb_hack.backend.domain.document.dto.DocumentResponseDto;
import kb_hack.backend.domain.document.mapper.DocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChecklistService {

	private final DocumentMapper documentMapper;

	/** (기존) 회원의 즐겨찾기 전체 공고에 대해 제출서류 리스트 */
	@Transactional(readOnly = true)
	public List<DocumentResponseDto> getChecklist(Long memberId) {
		return documentMapper.findDocumentsByMemberId(memberId);
	}

	/** ✅ 공고별 체크리스트 */
	@Transactional(readOnly = true)
	public List<DocumentResponseDto> getChecklistByAnnounce(Long memberId, Long announceId) {
		return documentMapper.findDocumentsByMemberAndAnnounce(memberId, announceId);
	}

	/** (기존) 단건 체크/해제 */
	@Transactional
	public void updateCheckStatus(Long memberId, Long documentId, DocumentCheckRequestDto dto) {
		documentMapper.upsertDocumentCheck(documentId, memberId, dto.isChecked());
	}

	/** ✅ 공고별 배치 체크/해제 저장 */
	@Transactional
	public void updateCheckStatusBulk(Long memberId, Long announceId, DocumentCheckBulkRequestDto bulkDto) {
		if (bulkDto == null || bulkDto.getItems() == null) return;
		for (DocumentCheckItemDto item : bulkDto.getItems()) {
			if (item == null || item.getDocumentId() == null) continue;
			documentMapper.upsertDocumentCheck(item.getDocumentId(), memberId, item.isChecked());
		}
	}
}
