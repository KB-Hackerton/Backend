package kb_hack.backend.domain.notice.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NoticeDetailDTO {
    private String title;
    private Long noticeId;
    private String content;
    private Date createdAt;

}
