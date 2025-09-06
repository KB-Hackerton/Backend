package kb_hack.backend.domain.festival;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Festival {
    private Long festivalId;        // festival_id (PK)
    private String festivalTitle;   // festival_title
    private String add1;            // add1
    private String add2;            // add2
    private BigDecimal mapX;        // map_x
    private BigDecimal mapY;        // map_y
    private LocalDate eventStartdate; // event_startdate
    private LocalDate eventEnddate;   // event_enddate
    private String firstImage;      // first_image
    private String tel;             // tel
    private String overview;        // overview
    private String contentId;       // content_id (외부 TourAPI 고유키)
    private String telName;
    private LocalDateTime createdAt; // created_at
}
