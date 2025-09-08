package kb_hack.backend.domain.article.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kb_hack.backend.domain.article.dto.ArticleRequest;
import kb_hack.backend.domain.article.entity.Article;

@Mapper
public interface ArticleMapper {
    /**
     * 크롤링한 기사 정보를 DB에 삽입합니다.
     */
    void insertArticle(ArticleRequest article);

    /**
     * 주어진 URL이 DB에 이미 존재하는지 확인합니다.
     *
     * @return 존재하면 true, 아니면 false
     */
    boolean existsByArticleUrl(@Param("articleUrl") String articleUrl);

    List<Article> getArticles();
}
