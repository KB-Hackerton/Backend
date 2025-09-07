package kb_hack.backend.domain.article.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import kb_hack.backend.domain.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ArticleListResponse {
	@Schema(description = "기사 ID", example = "1")
	private Long articleId;
	@Schema(description = "기사 제목", example = "소상공인 지원 정책 안내")
	private String title;
	@Schema(description = "기사 내용", example = "소상공인을 위한 다양한 지원 정책이 마련되어 있습니다...")
	private String content;
	@Schema(description = "기사 카테고리", example = "정부지원사업")
	private String category;
	@Schema(description = "기사 발행일", example = "2025-10-01")
	private LocalDate publishedAt;
	@Schema(description = "기사 URL", example = "https://example.com/article/1")
	private String articleUrl;

	public static ArticleListResponse from(Article article) {
		return new ArticleListResponse(
			article.getArticleId(),
			article.getTitle(),
			article.getContent(),
			article.getCategory(),
			LocalDate.from(article.getPublishedAt()),
			article.getArticleUrl()
		);
	}

}
