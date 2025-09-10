package kb_hack.backend.domain.SosChatting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChattingRoomListItem {
    private long chatRoomId;
    private String roomName;
    private int unreadCount;
    private Long lastMessageId;
    private String lastMessageSenderId;
    private String lastMessageContent;
    private LocalDateTime lastMessageAt;
}