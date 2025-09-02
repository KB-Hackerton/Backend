package kb_hack.backend.domain.article.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrawlRequestDto {

    @NotBlank(message = "크롤링할 URL은 비워둘 수 없습니다.")
    @URL(message = "유효한 URL 형식이 아닙니다.")
    private String url;

}
