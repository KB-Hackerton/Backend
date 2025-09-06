package kb_hack.backend.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecentFestivalDTO {
    private String festivalTitle;
    private Long festivalId;

}
