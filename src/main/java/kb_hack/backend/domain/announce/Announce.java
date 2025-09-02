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
//create table if not exists hack.announce
//        (
//                announce_id      bigint auto_increment
//                        primary key,
//                announce_title   varchar(255)                       null,
//link             varchar(255)                       null,
//author           varchar(50)                        null,
//exc_InsttNm      varchar(50)                        null,
//description      varchar(255)                       null,
//lcategory        varchar(50)                        null,
//pub_date         date                               null,
//reqst_start_date date                               null,
//reqst_end_date   date                               null,
//taget_name       varchar(50)                        null,
//view_num         int                                null,
//file_path_name   varchar(255)                       null,
//file_name        varchar(255)                       null,
//hashtags         varchar(255)                       null,
//how_to_register  varchar(100)                       null,
//call_company     varchar(255)                       null,
//created_at       datetime default CURRENT_TIMESTAMP not null
//        );

