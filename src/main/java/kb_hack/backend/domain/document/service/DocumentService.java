// kb_hack/backend/domain/document/service/DocumentService.java
package kb_hack.backend.domain.document.service;

import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.favorite.mapper.FavoriteMapper;
import kb_hack.backend.domain.document.dto.DocumentRequestDto;
import kb_hack.backend.domain.document.mapper.DocumentMapper;
import kb_hack.backend.domain.document.util.PdfTextExtractor;
import kb_hack.backend.domain.document.util.HttpFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			// ✅ print_file_path_name 우선 사용, 없으면 기존 file_path_name
			String url = firstNonBlank(announce.getPrintFilePathName(), announce.getFilePathName());
			if (url == null || url.isBlank()) continue;

			try {
				HttpFileUtil.Fetched f = HttpFileUtil.fetchToTemp(url);

				// ✅ PDF만 처리
				boolean isPdf = (f.contentType != null && f.contentType.toLowerCase().contains("pdf"))
					|| (f.fileName != null && f.fileName.toLowerCase().endsWith(".pdf"));
				if (!isPdf) {
					// JPG/PNG/HWP/HWPX는 skip
					continue;
				}

				String text = PdfTextExtractor.extract(f.file);
				List<String> docs = parseRequiredDocuments(text);
				for (String title : docs) {
					DocumentRequestDto dto = DocumentRequestDto.builder()
						.announceId(announce.getAnnounceId())
						.title(title)
						.description(null)
						.build();
					documentMapper.insertDocument(dto);
					savedCount++;
				}
			} catch (Exception e) {
				// 네트워크/파싱 실패시 해당 공고는 건너뜀 (로그만)
				e.printStackTrace();
			}
		}
		return savedCount;
	}

	private String firstNonBlank(String a, String b) {
		if (a != null && !a.isBlank()) return a;
		if (b != null && !b.isBlank()) return b;
		return null;
	}

	/**
	 * 제출서류 블록만 보수적으로 추출:
	 * - 앵커: 제출/신청/구비/첨부/필수 (서류/자료/문서) | 증빙자료
	 * - 다음 제목 or 다른 섹션 키워드 전까지 자르고, 불릿/번호 라인만 수집
	 */
	private List<String> parseRequiredDocuments(String text) {
		if (text == null || text.isBlank()) return List.of();
		String norm = normalize(text);

		Pattern ANCHOR = Pattern.compile("(제출|신청|구비|첨부|필수)\\s*(서류|자료|문서)|증빙\\s*자료");
		Matcher m = ANCHOR.matcher(norm);
		if (!m.find()) return List.of();

		int start = m.start();
		String tail = norm.substring(start, Math.min(norm.length(), start + 5000));

		// 다음 섹션 경계(흔한 제목/키워드)
		Pattern NEXT = Pattern.compile("(?m)^(지원대상|지원내용|신청방법|평가|선정|문의처|접수기간|유의사항)\\b");
		Matcher n = NEXT.matcher(tail);
		int end = n.find() ? n.start() : tail.length();

		String section = tail.substring(0, end);

		// 항목 라인 패턴(번호, 한글목차, 불릿)
		Pattern ITEM = Pattern.compile("(?m)^\\s*(?:\\d{1,2}[).]|[①-⑩]|[가-힣A-Za-z]\\)|[-–—•·●○▪◦])\\s+(.+?)\\s*$");
		Matcher it = ITEM.matcher(section);

		List<String> out = new ArrayList<>();
		while (it.find()) {
			String line = it.group(1).trim();
			// 흔한 꼬리표 제거
			line = line.replaceAll("\\s*\\(사본\\s*\\d+\\s*부\\)\\s*$", "").trim();
			if (!line.isBlank()) out.add(line);
		}
		// 정확도 위해 최소 2개 이상일 때만 인정
		return out.size() >= 2 ? out.stream().distinct().toList() : List.of();
	}

	private String normalize(String s) {
		return s.replace('\u00A0', ' ')
			.replace("\r", "\n")
			.replaceAll("\\t", " ")
			.replaceAll(" {2,}", " ");
	}
}
