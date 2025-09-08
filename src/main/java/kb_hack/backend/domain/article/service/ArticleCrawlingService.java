package kb_hack.backend.domain.article.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleCrawlingService {

	private final CrawlingOne crawlingOne; // 새로운 크롤러 주입
	private static final Set<String> ACRTICLE_SET = new HashSet<>();


	static {
		ACRTICLE_SET.add("https://www.sbma.kr/news_gisa/gisa_list.htm?gisa_category=01010000");
		ACRTICLE_SET.add("https://www.sbma.kr/news_gisa/gisa_list.htm?gisa_category=01020000");
		ACRTICLE_SET.add("https://www.sbma.kr/news_gisa/gisa_list.htm?gisa_category=01040000");
	}


	public void startCrawling() {
		for (String url : ACRTICLE_SET) {
			crawlingOne.crawlingOneArticle(url);
		}
	}


}
