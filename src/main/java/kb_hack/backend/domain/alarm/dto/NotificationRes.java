package kb_hack.backend.domain.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRes {
    private Long notificationId;
    private String title;
    private String content;
    private String notiType;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
