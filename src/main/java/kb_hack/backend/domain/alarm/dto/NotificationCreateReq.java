package kb_hack.backend.domain.alarm.dto;

import lombok.Data;

@Data
public class NotificationCreateReq {
    private String targetEmail;
    private String title;
    private String content;
    private String notiType;
}
