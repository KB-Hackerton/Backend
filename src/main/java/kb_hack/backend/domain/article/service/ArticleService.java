package kb_hack.backend.domain.article.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kb_hack.backend.domain.article.dto.response.ArticleListResponse;
import kb_hack.backend.domain.article.entity.Article;
import kb_hack.backend.domain.article.mapper.ArticleMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleMapper articleMapper;

	public List<ArticleListResponse> getArticles() {
		List<Article> articles = articleMapper.getArticles();
		return articles.stream()
			.map(ArticleListResponse::from)
			.toList();
	}



}
