package kb_hack.backend.domain.festival.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import kb_hack.backend.domain.festival.Festival;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class festivalDetailDto {
    private String festivalTitle;
    private String addr;
    private BigDecimal mapX;
    private BigDecimal mapY;
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate eventStartdate;
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate eventEnddate;
    private String firstImage;
    private String tel;
    private String overview;
    private String telName;

    public static festivalDetailDto from(Festival festival) {
        return  festivalDetailDto.builder()
                .festivalTitle(festival.getFestivalTitle())
                .addr(festival.getAdd1()+" "+festival.getAdd2())
                .mapX(festival.getMapX())
                .mapY(festival.getMapY())
                .eventStartdate(festival.getEventStartdate())
                .eventEnddate(festival.getEventEnddate())
                .firstImage(festival.getFirstImage())
                .tel(festival.getTel())
                .overview(festival.getOverview())
                .telName(festival.getTelName())
                .build();
    }

}
