package kb_hack.backend.domain.announce.dto;

import lombok.Data;

@Data
public class AnnounceRankingDTO {
    private Long announceId;
    private Integer viewNum;
    private String announceTitle;
}
