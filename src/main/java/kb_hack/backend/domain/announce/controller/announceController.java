package kb_hack.backend.domain.announce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.announce.dto.AnnounceRankingDTO;
import kb_hack.backend.domain.announce.dto.announceDetailDto;
import kb_hack.backend.domain.announce.dto.announceDto;
import kb_hack.backend.domain.announce.service.AnnounceRankingService;
import kb_hack.backend.domain.announce.service.announceService;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.exception.type.BadRequestException;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Announce", description = "공고 조회 API ")
@RestController
@RequestMapping("/announce")
@RequiredArgsConstructor
public class announceController {

    private final announceService announceService;
    private final AnnounceRankingService announceRankingService;

    @Operation(
            summary = "공고 목록 조회",
            description = "전체 공고 리스트를 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public SuccessResponse<List<announceDto>> getAllAnnounces() {
        List<announceDto> announces = announceService.getAnnounceList();
        return SuccessResponse.makeResponse(SuccessStatusCode.ANNOUNCE_GET_SUCCESS, announces);
    }

    @Operation(
            summary = "공고 상세 조회",
            description = "특정 공고 ID에 해당하는 상세 정보를 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{announceId}")
    public SuccessResponse<announceDetailDto> getAnnounceDetail(@PathVariable("announceId") Long announceId) {
        announceDetailDto dto = announceService.getAnnounceDetail(announceId);
        if (dto == null) {
            throw new BadRequestException(BadStatusCode.ANNOUNCE_NOT_FOUND);
        }
        return SuccessResponse.makeResponse(SuccessStatusCode.ANNOUNCE_GET_SUCCESS, dto);
    }

    @PostMapping("/test")
    public SuccessResponse<List<AnnounceRankingDTO>> test(){
        return SuccessResponse.makeResponse(SuccessStatusCode.EMAIL_VERIFY_CODE_SUCCESS,announceRankingService.getTopN(5));
    }

}
