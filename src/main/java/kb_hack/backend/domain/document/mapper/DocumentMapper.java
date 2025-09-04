package kb_hack.backend.domain.document.mapper;

import kb_hack.backend.domain.document.dto.DocumentRequestDto;
import kb_hack.backend.domain.document.dto.DocumentResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DocumentMapper {
	void insertDocument(DocumentRequestDto dto);

	// 전체(회원의 즐겨찾기 전체)
	List<DocumentResponseDto> findDocumentsByMemberId(@Param("memberId") Long memberId);

	// ✅ 공고별
	List<DocumentResponseDto> findDocumentsByMemberAndAnnounce(
		@Param("memberId") Long memberId,
		@Param("announceId") Long announceId);

	void upsertDocumentCheck(@Param("documentId") Long documentId,
		@Param("memberId") Long memberId,
		@Param("status") boolean status);
}
