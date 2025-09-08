package kb_hack.backend.domain.notice.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class NoticeDTO {
    private String title;
    private String content;
    private Date createdAt;
}
