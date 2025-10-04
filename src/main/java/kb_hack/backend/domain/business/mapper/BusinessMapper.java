package kb_hack.backend.domain.business.mapper;

import kb_hack.backend.domain.business.BusinessPlus;
import kb_hack.backend.domain.business.dto.BusinessDTO;
import kb_hack.backend.domain.member.dto.request.MemberInfoRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface BusinessMapper {
    int insertBusiness(BusinessDTO businessVO);
    long findBusinessClassIdByMinorname(String minorName);
    int updateBusiness(@Param("dto") MemberInfoRequestDTO dto,
                       @Param("businessId") Long businessId);
    String findMinorNameByBusinessId(Long businessId);

    // 📢 AI 추천 서비스에서 사용할 새로운 메서드
    Optional<BusinessPlus> findBusinessAndClassInfoByMemberId(@Param("memberId") Long memberId);
}
