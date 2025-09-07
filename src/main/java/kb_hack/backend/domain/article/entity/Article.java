package kb_hack.backend.domain.article.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

	private Long articleId;
	private String title;
	private String content;
	private String category;
	private String author;
	private LocalDateTime publishedAt;
	private String articleUrl;
	private LocalDateTime createdAt;


}
