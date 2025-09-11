package kb_hack.backend.domain.chat.dto.response;

import java.time.LocalDateTime;

import kb_hack.backend.domain.sos.entity.SosType;
import lombok.Data;

// ChatRoomDetailQueryResult.java (Mapper가 이 타입으로 결과를 반환)
@Data
public class ChatRoomDetailQueryResult {
    // ChatRoom, Sos, Owner 정보
    private String roomName;
    private Long sosId;
    private SosType sosType;
    private Boolean isComplete;
    private LocalDateTime createdAt;
    private Long ownerId;
    private String partnerBadge;


    // 상대방 정보
    private String partnerName; // bussinessName 대신 사용할 이름
    private String partnerImage;
}
