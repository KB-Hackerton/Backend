package kb_hack.backend.domain.festival.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.festival.dto.festivalDetailDto;
import kb_hack.backend.domain.festival.dto.festivalDto;
import kb_hack.backend.domain.festival.service.festivalService;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "축제 API", description = "축제 정보 조회 기능 제공 ")
@RestController
@RequestMapping("/festival")
@RequiredArgsConstructor
public class FestivalController {

    private final festivalService festivalService;

    @Operation(
            summary = "축제 목록 조회",
            description = "등록된 모든 축제 정보를 조회합니다. JWT 인증 필요.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public SuccessResponse<List<festivalDto>> getAllFestival() {
        List<festivalDto> festivalDtos = festivalService.getFestivalList();
        return SuccessResponse.makeResponse(SuccessStatusCode.FESTIVAL_GET_SUCCESS, festivalDtos);
    }

    @Operation(
            summary = "축제 상세 조회",
            description = "특정 축제 ID에 해당하는 상세 정보를 조회합니다. JWT 인증 필요.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{festivalId}")
    public SuccessResponse<festivalDetailDto> getFestivalDetail(@PathVariable Long festivalId) {
        return SuccessResponse.makeResponse(
                SuccessStatusCode.FESTIVAL_GET_SUCCESS,
                festivalService.getFestivalDetail(festivalId)
        );
    }
}
