package kb_hack.backend.domain.document.service;

import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.favorite.mapper.FavoriteMapper;
import kb_hack.backend.domain.document.dto.DocumentRequestDto;
import kb_hack.backend.domain.document.mapper.DocumentMapper;
import kb_hack.backend.domain.document.util.PdfTextExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

	private final FavoriteMapper favoriteMapper;
	private final DocumentMapper documentMapper;

	@Transactional
	public int extractAndSaveDocumentsFromFavorites(Long memberId) {
		int savedCount = 0;

		List<Announce> favorites = favoriteMapper.findAnnouncesByMemberId(memberId);

		for (Announce announce : favorites) {
			String filePath = announce.getFilePathName();
			if (filePath == null || !filePath.toLowerCase().endsWith(".pdf")) continue;

			try {
				String extractedText = PdfTextExtractor.extract(new File(filePath));
				List<String> requiredDocs = parseRequiredDocuments(extractedText);

				for (String doc : requiredDocs) {
					DocumentRequestDto dto = DocumentRequestDto.builder()
						.announceId(announce.getAnnounceId())
						.title(doc.trim())
						.description(null)
						.build();
					documentMapper.insertDocument(dto);
					savedCount++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return savedCount;
	}

	private List<String> parseRequiredDocuments(String text) {
		if (text == null) return List.of();

		// TODO: 필요시 키워드 정교화
		String lower = text.toLowerCase();
		if (lower.contains("제출 서류") || lower.contains("구비서류") || lower.contains("신청서류")) {
			return Arrays.asList(text.split("\n")).subList(0, Math.min(5, text.split("\n").length));
		}
		return List.of();
	}
}
