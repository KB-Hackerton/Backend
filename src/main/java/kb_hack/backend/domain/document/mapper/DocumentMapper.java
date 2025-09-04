package kb_hack.backend.domain.document.mapper;

import kb_hack.backend.domain.document.dto.DocumentRequestDto;
import kb_hack.backend.domain.document.dto.DocumentResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DocumentMapper {
	void insertDocument(DocumentRequestDto dto);

	List<DocumentResponseDto> findDocumentsByMemberId(@Param("memberId") Long memberId);

	void upsertDocumentCheck(@Param("documentId") Long documentId,
		@Param("memberId") Long memberId,
		@Param("status") boolean status);
}

