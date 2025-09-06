package kb_hack.backend.domain.announce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kb_hack.backend.domain.announce.Announce;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class announceDto {
    private Long announceId;
    private String title;
    private String excInsttNm; // 발행기관
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate endDate;
    private boolean isFavorite;

    //announce => AnnounceDto
    public static announceDto from(Announce announce,boolean isFavorite) {
        return  announceDto.builder()
                .announceId(announce.getAnnounceId())
                .title(announce.getAnnounceTitle())
                .excInsttNm(announce.getExcInsttNm())
                .startDate(announce.getStartDate())
                .endDate(announce.getEndDate())
                .isFavorite(false)
                .build();
    }
}
