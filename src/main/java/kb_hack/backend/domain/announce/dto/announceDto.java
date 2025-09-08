package kb_hack.backend.domain.announce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import kb_hack.backend.domain.announce.Announce;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class announceDto {
    private Long announceId;
    private String title;
    private String excInsttNm; // 발행기관
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate endDate;
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate pubDate;
    private boolean isFavorite;

    //announce => AnnounceDto
    public static announceDto from(Announce announce,boolean isFavorite) {
        return  announceDto.builder()
                .announceId(announce.getAnnounceId())
                .title(announce.getAnnounceTitle())
                .excInsttNm(announce.getExcInsttNm())
                .startDate(announce.getStartDate())
                .endDate(announce.getEndDate())
                .pubDate(announce.getPubDate())
                .isFavorite(isFavorite)
                .build();
    }
}
