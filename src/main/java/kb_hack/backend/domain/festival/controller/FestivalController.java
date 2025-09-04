package kb_hack.backend.domain.festival.controller;

import kb_hack.backend.domain.announce.dto.announceDetailDto;
import kb_hack.backend.domain.announce.dto.announceDto;
import kb_hack.backend.domain.announce.service.announceService;
import kb_hack.backend.domain.festival.dto.festivalDetailDto;
import kb_hack.backend.domain.festival.dto.festivalDto;
import kb_hack.backend.domain.festival.service.festivalService;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.exception.type.BadRequestException;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/festival")
@RequiredArgsConstructor
public class FestivalController {
    private final festivalService festivalService;

    @GetMapping
    public SuccessResponse<List<festivalDto>> getAllFestival() {
        List<festivalDto> festivalDtos =  festivalService.getFestivalList();
        return SuccessResponse.makeResponse(SuccessStatusCode.FESTIVAL_GET_SUCCESS, festivalDtos);
    }
    @GetMapping("/{festivalId}")
    public SuccessResponse<festivalDetailDto> getFestivalDetail(@PathVariable Long festivalId) {

        return SuccessResponse.makeResponse(SuccessStatusCode.FESTIVAL_GET_SUCCESS, festivalService.getFestivalDetail(festivalId));
    }

}


