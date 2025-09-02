package kb_hack.backend.domain.article.service;

import static kb_hack.backend.global.common.exception.enums.BadStatusCode.*;
import static kb_hack.backend.global.common.exception.enums.BadStatusCode.CRAWL_FAIL_EXCEPTION;
import static kb_hack.backend.global.common.exception.enums.BadStatusCode.CRAWL_URL_FAIL_EXCEPTION;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import kb_hack.backend.domain.article.dto.ArticleRequest;
import kb_hack.backend.domain.article.mapper.ArticleMapper;
import kb_hack.backend.global.common.exception.type.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j // Slf4j 어노테이션으로 로거 자동 생성
@Service
@RequiredArgsConstructor
public class CrawlingOne {

	private final ArticleMapper articleMapper;
	private static final Map<String, String> CATEGORY_MAP = new HashMap<>();
	static {
		CATEGORY_MAP.put("01010000", "소상공인");
		CATEGORY_MAP.put("01020000", "농업인소식");
		CATEGORY_MAP.put("01040000", "정부지원사업");
	}

	@Async
	public void crawlingOneArticle(String targetUrl) {
		log.info("요청 URL에 대한 크롤링을 시작합니다: {}", targetUrl);
		String baseUrl = "https://www.sbma.kr/";
		int pagesToCrawl = 2; // 2페이지 크롤링

		String categoryCode = getQueryParam(targetUrl, "gisa_category");
		String category = CATEGORY_MAP.getOrDefault(categoryCode, "기타");

		// URL에 page 파라미터가 이미 있는지 확인하여 시작 페이지 결정
		int startPage = 1;
		String pageParam = getQueryParam(targetUrl, "page");
		if (pageParam != null && !pageParam.isEmpty()) {
			startPage = Integer.parseInt(pageParam);
			pagesToCrawl = startPage; // 특정 페이지만 크롤링하도록 설정
		}

		try {
			for (int page = startPage; page <= pagesToCrawl; page++) {
				// 페이지네이션 URL 구성
				String paginatedUrl = targetUrl.replaceAll("&page=\\d+", "") + "&page=" + page;
				log.info("--- {} 페이지 크롤링 중... ---", page);

				Document doc = Jsoup.connect(paginatedUrl).get();
				Elements articleList = doc.select("div.glores-A-news-webzine > ul > li");

				if (articleList.isEmpty()) {
					log.info("페이지에 더 이상 기사가 없어 크롤링을 중단합니다.");
					break;
				}

				// ... 이전과 동일한 크롤링 및 저장 로직 ...
				for (Element item : articleList) {
					Element linkTag = item.selectFirst("a");
					if (linkTag == null)
						continue;

					String articleUrl = new URL(new URL(baseUrl), linkTag.attr("href")).toString();
					if (articleMapper.existsByArticleUrl(articleUrl)) {
						log.warn("이미 수집된 기사입니다. 건너뜁니다: {}", articleUrl);
						continue;
					}

					Element captionBox = item.selectFirst("span.caption");
					if (captionBox == null)
						continue;

					String title = captionBox.selectFirst("span.title").text();
					String dateStr = captionBox.selectFirst("span.date").text();
					LocalDateTime publishedAt = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy.MM.dd"))
						.atStartOfDay();

					// 1. .cont.pc_show 클래스로 요약 내용을 가져옵니다.
					Element summaryElement = captionBox.selectFirst("span.cont.pc_show");
					String content = summaryElement != null ? summaryElement.text() : "";

					// 2. 상세 페이지를 방문하지 않으므로, author는 기본값으로 설정합니다.
					String author = "(사)한국소상공인마케팅협회";

					ArticleRequest articleDto = ArticleRequest.builder()
						.title(title)
						.content(content)
						.category(category)
						.author(author)
						.publishedAt(publishedAt)
						.articleUrl(articleUrl)
						.build();

					articleMapper.insertArticle(articleDto);
				}
			}
		} catch (IOException e) {
			throw new CustomException(CRAW_IO_EXCEPTION);
		} catch (Exception e) {
			throw new CustomException(CRAWL_FAIL_EXCEPTION);
		}
		log.info("크롤링 작업이 완료되었습니다: {}", targetUrl);
	}

	private String getQueryParam(String url, String paramName) {
		try {
			String query = new URL(url).getQuery();
			if (query == null || query.isEmpty()) {
				return "";
			}

			return Arrays.stream(query.split("&"))          // "param=value" 형태의 배열로 변환
				.filter(p -> p.startsWith(paramName + "=")) // 원하는 파라미터 이름으로 시작하는지 필터링
				.map(p -> p.split("=")[1])           // "="를 기준으로 분리하여 값(value) 부분만 추출
				.findFirst()                                       // 첫 번째로 찾은 값 반환
				.orElse("");                                // 값이 없으면 빈 문자열 반환

		} catch (MalformedURLException e) {
			throw new CustomException(CRAWL_URL_FAIL_EXCEPTION);
		}
	}

}
