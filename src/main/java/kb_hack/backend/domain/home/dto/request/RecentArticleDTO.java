package kb_hack.backend.domain.home.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecentArticleDTO {
    private String title;
    private Long articleId;
}
