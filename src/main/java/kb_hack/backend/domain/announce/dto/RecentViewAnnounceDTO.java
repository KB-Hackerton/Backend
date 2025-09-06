package kb_hack.backend.domain.announce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecentViewAnnounceDTO {
    private Long announceId;
    private String announceTitle;
}
