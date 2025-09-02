package kb_hack.backend.domain.article.controller;

import static kb_hack.backend.global.common.exception.enums.SuccessStatusCode.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kb_hack.backend.domain.article.service.ArticleCrawlingService;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;

@RequestMapping("/articles")
@RequiredArgsConstructor
@RestController
public class CrawlController {

    private final ArticleCrawlingService articleCrawlingService;

    /**
     * 특정 URL에 대한 크롤링을 시작하는 공개 API
     * @param requestDto 크롤링할 URL이 담긴 DTO
     * @return 요청 접수 완료 응답
     */
    @PostMapping("/crawl/admin")
    // @PreAuthorize("hasRole('ADMIN')")  <- 관리자 권한 체크 어노테이션 제거!
    public SuccessResponse startCrawling() {

        // 서비스의 비동기 메서드를 호출
        articleCrawlingService.startCrawling();

        // 클라이언트에게는 크롤링 완료를 기다리지 않고 바로 응답
        return SuccessResponse.makeResponse(CRAWL_ARTICLE_SUCCESS);
    }

}
