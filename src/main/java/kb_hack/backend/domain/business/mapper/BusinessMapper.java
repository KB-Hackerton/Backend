package kb_hack.backend.domain.business.mapper;

import kb_hack.backend.domain.business.dto.BusinessDTO;
import kb_hack.backend.domain.member.dto.request.MemberInfoRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BusinessMapper {
    int insertBusiness(BusinessDTO businessVO);
    long findBusinessClassIdByMinorname(String minorName);
    int updateBusiness(@Param("dto") MemberInfoRequestDTO dto,
                       @Param("businessId") Long businessId);}
