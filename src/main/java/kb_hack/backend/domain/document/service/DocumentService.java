// kb_hack/backend/domain/document/service/DocumentService.java
package kb_hack.backend.domain.document.service;

import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.favorite.mapper.FavoriteMapper;
import kb_hack.backend.domain.document.dto.DocumentRequestDto;
import kb_hack.backend.domain.document.mapper.DocumentMapper;
import kb_hack.backend.domain.document.util.HttpFileUtil;
import kb_hack.backend.domain.document.util.PdfTextExtractor;
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

	/**
	 * 즐겨찾기한 공고의 print_file_path_name(없으면 file_path_name)에서 PDF만 내려받아
	 * '제출서류' 섹션을 파싱하고 document 테이블에 저장
	 */
	@Transactional
	public int extractAndSaveDocumentsFromFavorites(Long memberId) {
		int savedCount = 0;

		List<Announce> favorites = favoriteMapper.findAnnouncesByMemberId(memberId);
		for (Announce announce : favorites) {
			String url = firstNonBlank(announce.getPrintFilePathName(), announce.getFilePathName());
			if (url == null || url.isBlank()) continue;

			try {
				HttpFileUtil.Fetched f = HttpFileUtil.fetchToTemp(url);

				// ✅ PDF만 처리
				boolean isPdf = (f.contentType != null && f.contentType.toLowerCase().contains("pdf"))
					|| (f.fileName != null && f.fileName.toLowerCase().endsWith(".pdf"));
				if (!isPdf) {
					// HWP/HWPX/이미지 등은 스킵
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
				// 네트워크/파싱 실패는 해당 공고만 건너뜀
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
	 * 제출서류 섹션 파싱(정확도 강화):
	 * - 앵커: (제출|신청|구비|첨부|필수)(서류|자료|문서) or 증빙자료
	 * - 다음 섹션 경계(지원대상/신청방법/평가/문의처 등) 전까지만
	 * - 불릿/번호 감지 + 줄 병합
	 * - 화이트/블랙리스트 필터
	 * - 최소 개수(2개) 미만이면 폐기
	 */
	private List<String> parseRequiredDocuments(String text) {
		if (text == null || text.isBlank()) return List.of();

		String norm = normalize(text);

		// 1) 앵커 탐지
		Pattern ANCHOR = Pattern.compile("(제출|신청|구비|첨부|필수)\\s*(서류|자료|문서)|증빙\\s*자료");
		Matcher m = ANCHOR.matcher(norm);
		if (!m.find()) return List.of();

		int start = m.start();
		String tail = norm.substring(start, Math.min(norm.length(), start + 8000)); // 섹션 길이 상한

		// 2) 다음 섹션 경계
		Pattern NEXT = Pattern.compile("(?m)^(지원대상|지원내용|신청방법|평가|선정|문의처|접수기간|유의사항|세부내용|추진절차|추진일정)\\b");
		Matcher n = NEXT.matcher(tail);
		int end = n.find() ? n.start() : tail.length();
		String section = tail.substring(0, end);

		// 3) 줄 병합(불릿 다음 들여쓰기/붙임줄 합치기)
		List<String> lines = mergeWrappedLines(section);

		// 4) 필터링
		List<String> white = List.of(
			"증명서","확인서","신청서","계획서","등록증","사본","명부","명세서","증빙",
			"납부서","증명원","인감","위임장","재무제표","견적서","제출서","계약서",
			"통장사본","사업자등록증","등본","초본","포트폴리오","재직증명서","4대보험",
			"납부확인","면허증","허가증","수료증","졸업증명서","성적증명서","서약서"
		);
		List<String> black = List.of(
			"지원대상","지원내용","신청방법","평가","선정","문의처","접수기간","유의사항",
			"절차","일정","프로세스","평가위원","선정평가","문의","전화","이메일","전자문서",
			"http","www.","@","접수","신청·접수","신청 접수","발표","협약","수행","사업수행",
			"선정통보","종합평점","운영사","보육","멘토","추천","사전검토","프로그램","세부내용"
		);

		List<String> items = new ArrayList<>();
		for (String raw : lines) {
			String s = raw.trim();

			// 무의미 패턴 제거
			if (s.isBlank()) continue;
			if (s.matches("^[0-9]+\\s*[-–—]?\\s*$")) continue;      // "7 -" 등
			if (s.length() <= 2) continue;

			// 블랙리스트 제외
			boolean hasBlack = black.stream().anyMatch(s::contains);
			if (hasBlack) continue;

			// 화이트리스트 포함 + 불릿/번호 라인 or 문장형 서류
			boolean hasWhite = white.stream().anyMatch(s::contains);
			boolean bulletish = s.matches("^\\s*(?:\\d{1,2}[).]|[①-⑳]|[가-힣A-Za-z]\\)|[-–—•·●○▪◦]).{2,}");
			if (hasWhite && (bulletish || s.endsWith("서") || s.endsWith("증") || s.endsWith("서류"))) {
				// 흔한 꼬리표 정리
				s = s.replaceAll("\\s*\\(사본\\s*\\d+\\s*부\\)\\s*$", "").trim();
				s = s.replaceAll("\\s{2,}", " ");
				items.add(s);
			}
		}

		// 중복 제거
		items = items.stream().distinct().toList();

		// 신뢰도: 2개 미만이면 노이즈로 판단
		if (items.size() < 2) return List.of();

		return items;
	}

	/** 불릿라인 뒤의 들여쓰기/붙임줄을 같은 항목으로 병합 */
	private List<String> mergeWrappedLines(String section) {
		String[] rawLines = section.split("\\r?\\n");
		List<String> out = new ArrayList<>();

		StringBuilder cur = new StringBuilder();
		Pattern BULLET = Pattern.compile("^\\s*(?:\\d{1,2}[).]|[①-⑳]|[가-힣A-Za-z]\\)|[-–—•·●○▪◦])\\s+.+$");

		for (String ln : rawLines) {
			String line = ln.replace('\u00A0', ' ').trim();

			if (BULLET.matcher(line).find()) {
				// 새 불릿 시작: 이전 항목 flush
				if (cur.length() > 0) {
					out.add(cur.toString().trim());
					cur.setLength(0);
				}
				cur.append(line);
			} else {
				// 이어 붙이기
				if (cur.length() > 0 && !line.isBlank()) {
					cur.append(' ').append(line);
				} else if (!line.isBlank()) {
					// 섹션 첫 줄이 제목형인 경우 임시 시작
					cur.append(line);
				}
			}
		}
		if (cur.length() > 0) out.add(cur.toString().trim());
		return out;
	}

	private String normalize(String s) {
		return s.replace('\u00A0', ' ')
			.replace("\r", "\n")
			.replaceAll("\\t", " ")
			.replaceAll(" {2,}", " ");
	}
}
