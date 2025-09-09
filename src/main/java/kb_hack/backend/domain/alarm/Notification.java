package kb_hack.backend.domain.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private Long notificationId;
    private Long memberId;
    private String title;
    private String content;
    private String notiType;   // 'all','notice','sos','announce','festival'
    private Boolean isRead;
    private LocalDateTime createdAt;
}
