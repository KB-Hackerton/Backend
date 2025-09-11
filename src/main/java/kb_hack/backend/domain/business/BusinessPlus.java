package kb_hack.backend.domain.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessPlus {
    private Long businessId;
    private Long businessClassId;
    private String businessNm;
    private String businessAddr;
    private String businessAddrDetail;
    private String businessCode;
    private LocalDate businessOpenDate;
    private LocalDateTime createdAt;
    private Long memberId;

    // These fields are for mapping data from the JOIN query in the mapper
    private String businessClassMajorName;
    private String businessClassMiddleName;
    private String businessClassMinorName;
}
