package kb_hack.backend.domain.announce;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Announce {
private Long announceId;
private String announceTitle;
private String link;
private String author;
private String excInsttNm;
private String description;
private String lcategory;
private LocalDate pubDate;
private LocalDate startDate;
private LocalDate endDate;
private String targetName;
private Integer viewNum;
private String filePathName;
private String fileName;
private String howToRegister;
private String callCompany;
}

