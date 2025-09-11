package kb_hack.backend.domain.SosChatting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnreadCountDTO {
    private Long chatRoomId;
    private Integer unreadCount;
}
