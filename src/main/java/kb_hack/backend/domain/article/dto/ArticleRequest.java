package kb_hack.backend.domain.article.dto;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ArticleRequest {
    private String title;
    private String content;
    private String category;
    private String author;
    private LocalDateTime publishedAt;
    private String articleUrl;
}
