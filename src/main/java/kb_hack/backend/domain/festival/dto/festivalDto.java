package kb_hack.backend.domain.festival.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import kb_hack.backend.domain.festival.Festival;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class festivalDto {
    private Long festivalId;
    private String festivalTitle;
    private String addr;
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate eventStartdate;
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate eventEnddate;
    private String first_image;

    public static festivalDto from(Festival festival) {
        return  festivalDto.builder()
                .festivalId(festival.getFestivalId())
                .festivalTitle(festival.getFestivalTitle())
                .addr(festival.getAdd1()+" "+festival.getAdd2())
                .eventStartdate(festival.getEventStartdate())
                .eventEnddate(festival.getEventEnddate())
                .first_image(festival.getFirstImage())
                .build();
    }
}
