// kb_hack/backend/domain/document/service/DocumentService.java
package kb_hack.backend.domain.document.service;

import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.announce.mapper.AnnounceMapper;
import kb_hack.backend.domain.favorite.mapper.FavoriteMapper;
import kb_hack.backend.domain.document.dto.DocumentRequestDto;
import kb_hack.backend.domain.document.mapper.DocumentMapper;
import kb_hack.backend.domain.document.util.HttpFileUtil;
import kb_hack.backend.domain.document.util.PdfTextExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class DocumentService {

	private final FavoriteMapper favoriteMapper;
	private final DocumentMapper documentMapper;
	private final AnnounceMapper announceMapper;   // ✅ 단일 공고 조회용

	@Value("${openai.api.key}")
	private String openaiApiKey;

	/**
	 * 즐겨찾기한 공고들의 첨부(PDF)에서 '제출서류' 섹션을 추출해 저장
	 */
	@Transactional
	public int extractAndSaveDocumentsFromFavorites(Long memberId) {
		int savedCount = 0;

		List<Announce> favorites = favoriteMapper.findAnnouncesByMemberId(memberId);
		for (Announce announce : favorites) {
			savedCount += extractAndSaveInternal(announce);
		}
		return savedCount;
	}

	/**
	 * ✅ 특정 공고(announceId) 하나에서만 '제출서류' 섹션을 추출해 저장
	 */
	@Transactional
	public int extractAndSaveDocumentsForAnnounce(Long announceId) {
		Announce announce = announceMapper.findById(announceId);
		if (announce == null) return 0;
		return extractAndSaveInternal(announce);
	}

	@Transactional
	public int extractAndSaveDocumentsForAllAnnounces() {
		int savedCount = 0;
		List<Announce> announces = announceMapper.findAll(); // ✅ 모든 공고 조회 (이제 모든 컬럼 포함)
		for (Announce announce : announces) {
			savedCount += extractAndSaveInternal(announce);
		}
		return savedCount;
	}

	/** 공통 로직: 공고 1건에 대해 파일 내려받기 → PDF만 처리 → 텍스트 추출 → GPT 호출 → 저장 */
	private int extractAndSaveInternal(Announce announce) {
		String url = firstNonBlank(announce.getPrintFilePathName(), announce.getFilePathName());
		if (url == null || url.isBlank()) return 0;

		try {
			HttpFileUtil.Fetched f = HttpFileUtil.fetchToTemp(url);

			// PDF만 처리
			boolean isPdf = (f.contentType != null && f.contentType.toLowerCase().contains("pdf"))
				|| (f.fileName != null && f.fileName.toLowerCase().endsWith(".pdf"));
			if (!isPdf) return 0;

			String text = PdfTextExtractor.extract(f.file);

			// ✅ PDF 전체 텍스트 디버깅 출력
			System.out.println("=== PDF 전체 텍스트 (앞 5000자) ===");
			System.out.println(text);
			System.out.println("================================");

			// ✅ GPT에게 제출서류 목록 요청 (전체 텍스트 전달)
			List<String> docs = callGptForRequiredDocuments(text);

			int saved = 0;
			for (String title : docs) {
				DocumentRequestDto dto = DocumentRequestDto.builder()
					.announceId(announce.getAnnounceId())
					.title(title)
					.description(null)
					.build();
				documentMapper.insertDocument(dto);
				saved++;
			}
			return saved;
		} catch (Exception e) {
			// 실패 시 해당 공고만 스킵
			e.printStackTrace();
			return 0;
		}
	}

	private String firstNonBlank(String a, String b) {
		if (a != null && !a.isBlank()) return a;
		if (b != null && !b.isBlank()) return b;
		return null;
	}

	/**
	 * ✅ GPT 호출해서 제출서류만 추출
	 */
	/**
	 * ✅ GPT 호출해서 제출/신청 서류만 추출 (안전 파싱 + 디버깅 로그 포함)
	 */
	/**
	 * ✅ GPT 호출해서 제출/신청 서류만 추출 (마크다운 제거 + 안전 파싱)
	 */
	private List<String> callGptForRequiredDocuments(String text) throws Exception {
		HttpClient client = HttpClient.newHttpClient();
		ObjectMapper mapper = new ObjectMapper();

		// 프롬프트 강화
		String prompt = """
    아래 공고문 전체 텍스트에서 신청자가 제출해야 하는 '제출서류' 또는 '신청서류' 항목만 뽑아줘.
    ⚠️ 반드시 JSON 배열만 출력해. (예: ["참가신청서", "사업계획서", "개인정보 동의서"])
    ⚠️ 다른 설명, ```json 같은 마크다운 블록, 불필요한 텍스트는 절대 포함하지 마.

    문서 내용:
    """ + text;

		String body = mapper.writeValueAsString(
			new java.util.HashMap<>() {{
				put("model", "gpt-4o-mini");
				put("messages", List.of(
					java.util.Map.of("role", "system", "content", "너는 공고문에서 제출/신청 서류만 뽑는 AI야."),
					java.util.Map.of("role", "user", "content", prompt)
				));
				put("temperature", 0);
			}}
		);

		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://api.openai.com/v1/chat/completions"))
			.header("Content-Type", "application/json")
			.header("Authorization", "Bearer " + openaiApiKey)
			.POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
			.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		// ✅ GPT Raw Response 로그
		System.out.println("=== GPT Raw Response ===");
		System.out.println(response.body());
		System.out.println("========================");

		JsonNode root = mapper.readTree(response.body());
		JsonNode choices = root.path("choices");

		if (!choices.isArray() || choices.size() == 0) {
			throw new RuntimeException("❌ GPT 응답에 choices가 없음: " + response.body());
		}

		JsonNode contentNode = choices.get(0).path("message").path("content");
		if (contentNode.isMissingNode() || contentNode.asText().isBlank()) {
			throw new RuntimeException("❌ GPT 응답에서 content가 없음: " + response.body());
		}

		// GPT 응답 본문
		String content = contentNode.asText().trim();

		// ✅ 마크다운 제거
		content = content.replaceAll("(?s)```json", "")
			.replaceAll("(?s)```", "")
			.trim();

		// ✅ JSON 배열 파싱
		try {
			List<String> docs = new ArrayList<>();
			for (JsonNode node : mapper.readTree(content)) {
				docs.add(node.asText().trim());
			}
			return docs;
		} catch (Exception e) {
			// fallback (라인 단위 분리)
			System.out.println("⚠️ GPT 응답 JSON 파싱 실패, 라인 분리 fallback: " + content);
			return Arrays.stream(content.split("[\\r\\n]+"))
				.map(String::trim)
				.filter(s -> !s.isBlank())
				.toList();
		}
	}


}