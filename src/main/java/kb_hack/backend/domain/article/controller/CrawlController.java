package kb_hack.backend.domain.article.controller;

import static kb_hack.backend.global.common.exception.enums.SuccessStatusCode.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kb_hack.backend.domain.article.service.ArticleCrawlingService;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;

@Tag(name = "Article Crawl", description = "기사 크롤링 API (관리자용)")
@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class CrawlController {

    private final ArticleCrawlingService articleCrawlingService;

    /**
     * 특정 URL에 대한 크롤링을 시작하는 공개 API
     */
    @Operation(
            summary = "기사 크롤링 실행",
            description = "관리자 전용 API. 특정 URL에 대한 기사 크롤링을 시작합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/articles/crawl")
    public SuccessResponse startCrawling() {
        articleCrawlingService.startCrawling();
        return SuccessResponse.makeResponse(CRAWL_ARTICLE_SUCCESS);
    }

}
