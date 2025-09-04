package kb_hack.backend.domain.document.service;

import kb_hack.backend.domain.document.dto.DocumentCheckRequestDto;
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

	@Transactional(readOnly = true)
	public List<DocumentResponseDto> getChecklist(Long memberId) {
		return documentMapper.findDocumentsByMemberId(memberId);
	}

	@Transactional
	public void updateCheckStatus(Long memberId, Long documentId, DocumentCheckRequestDto dto) {
		documentMapper.upsertDocumentCheck(documentId, memberId, dto.isChecked());
	}
}
