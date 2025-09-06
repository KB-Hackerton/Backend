package kb_hack.backend.domain.business.service;

import kb_hack.backend.domain.business.dto.BusinessDTO;
import kb_hack.backend.domain.business.mapper.BusinessMapper;
import kb_hack.backend.domain.member.dto.request.SigunUpRequestDTO;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.BadRequestException;
import kb_hack.backend.global.common.exception.type.ServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
@Log4j2
@Service
@RequiredArgsConstructor
public class BusinessService {
    private final BusinessMapper businessMapper;

    public void sigunUpInsertBusinessInfo(SigunUpRequestDTO sigunUpRequestDTO,Long memberId ){

        if(sigunUpRequestDTO == null){
            throw new BadRequestException(BadStatusCode.EMPTY_SIGNUP_INFO_EXCEPTION);
        }
        try{
            BusinessDTO dto = new BusinessDTO().makeBusniessDTO(sigunUpRequestDTO);
            Long businessClassId = businessMapper.findBusinessClassIdByMinorname(sigunUpRequestDTO.getMinorName());

            if(businessClassId == null) {
                throw new BadRequestException(BadStatusCode.INVALID_MINOR_NAME_EXCEPTION);
            }

            dto.setBusinessClassId(businessClassId);
            dto.setMemberId(memberId);

            int i = businessMapper.insertBusiness(dto);

            if(i == 0){
                throw new BadRequestException(BadStatusCode.FAIL_TO_REGISTER_BUSINESS_EXCEPTION);
            }


        }catch (DataAccessException e){
            log.info(e.getMessage());
            throw new ServerErrorException(BadStatusCode.DATABASE_PROCESSING_EXCEPTION);
        }catch(Exception e){
            throw new ServerErrorException(BadStatusCode.INTERNAL_SERVER_EXCEPTION);
        }

    }
}
