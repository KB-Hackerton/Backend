package kb_hack.backend.domain.article.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.article.dto.response.ArticleListResponse;
import kb_hack.backend.domain.article.service.ArticleService;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Tag(name = "Article ", description = "기사 관련 API")
@RequestMapping("/articles")
@RequiredArgsConstructor
@RestController
public class ArticleController {

	private  final ArticleService articleService;

	@Operation(
		summary = "소상공인 관련 기사 목록 조회 ",
		description = "소상공인 관련 최신 기사 목록을 조회합니다.",
			security = @SecurityRequirement(name = "bearerAuth")

	)
	@GetMapping("/list")
	public SuccessResponse<List<ArticleListResponse>> getArticles() {
		List<ArticleListResponse> articles = articleService.getArticles();
		return SuccessResponse.makeResponse(SuccessStatusCode.ARTICLE_GET_SUCCESS, articles);
	}



}
