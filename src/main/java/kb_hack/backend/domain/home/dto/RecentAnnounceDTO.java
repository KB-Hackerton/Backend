package kb_hack.backend.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecentAnnounceDTO {

    private Date requestEndDate;
    private String announceTitle;
    private Long announceId;
}
