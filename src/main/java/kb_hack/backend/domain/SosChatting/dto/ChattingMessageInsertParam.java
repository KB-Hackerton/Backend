package kb_hack.backend.domain.SosChatting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChattingMessageInsertParam {
    private long id;       // useGeneratedKeys 로 세팅됨
    private long roomId;
    private long senderId;
    private String content;
}
