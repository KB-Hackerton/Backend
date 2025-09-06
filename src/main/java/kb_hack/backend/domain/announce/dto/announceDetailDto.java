package kb_hack.backend.domain.announce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import kb_hack.backend.domain.announce.Announce;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class announceDetailDto {
    private String announceTitle;
    private String link;
    private String author;
    private String excInsttNm;
    private String description;
    private String lcategory;
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate pubDate;
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate endDate;
    private String targetName;
    private Integer viewNum;
    private String filePathName;
    private String fileName;
    private String howToRegister;
    private String callCompany;
    private String printFilePathName;
    private String printFileName;


    public static announceDetailDto from(Announce announce) {
        return announceDetailDto.builder()
                .announceTitle(announce.getAnnounceTitle())
                .link(announce.getLink())
                .author(announce.getAuthor())
                .excInsttNm(announce.getExcInsttNm())
                .description(announce.getDescription())
                .lcategory(announce.getLcategory())
                .pubDate(announce.getPubDate())
                .startDate(announce.getStartDate())
                .endDate(announce.getEndDate())
                .targetName(announce.getTargetName())
                .viewNum(announce.getViewNum())
                .filePathName(announce.getFilePathName())
                .fileName(announce.getFileName())
                .howToRegister(announce.getHowToRegister())
                .callCompany(announce.getCallCompany())
                .printFilePathName(announce.getPrintFilePathName())
                .printFileName(announce.getPrintFileName())
                .build();
    }
}
