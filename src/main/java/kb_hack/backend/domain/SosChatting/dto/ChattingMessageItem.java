package kb_hack.backend.domain.SosChatting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChattingMessageItem {
    private long id;
    private String senderId;
    private String content;
    private LocalDateTime createdAt;
}
