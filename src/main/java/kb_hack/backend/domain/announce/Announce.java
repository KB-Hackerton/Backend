package kb_hack.backend.domain.announce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
private String hashtags;
private String targetName;
private Integer viewNum;
private String filePathName;
private String fileName;
private String printFilePathName;
private String printFileName;
private String howToRegister;
private String callCompany;
}

