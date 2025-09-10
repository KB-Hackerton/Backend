package kb_hack.backend.domain.chat.dto.request;

import java.util.List;

import lombok.Data;

// Request DTO: 요청 본문을 담을 객체
@Data
public class SosCompleteRequest {
    private List<Long> helperMemberIds;
}
