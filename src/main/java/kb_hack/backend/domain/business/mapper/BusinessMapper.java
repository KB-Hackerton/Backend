package kb_hack.backend.domain.business.mapper;

import kb_hack.backend.domain.business.dto.BusinessDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BusinessMapper {
    int insertBusiness(BusinessDTO businessVO);
    long findBusinessClassIdByMinorname(String minorName);
}
