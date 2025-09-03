package kb_hack.backend.domain.announce.controller;


import io.opentelemetry.api.trace.StatusCode;
import kb_hack.backend.domain.announce.dto.announceDetailDto;
import kb_hack.backend.domain.announce.dto.announceDto;
import kb_hack.backend.domain.announce.service.announceService;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.exception.type.BadRequestException;
import kb_hack.backend.global.common.response.bad.BadResponse;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/announce")
@RequiredArgsConstructor
public class announceController {
    private final announceService announceService;

    @GetMapping
    public SuccessResponse<List<announceDto>> getAllAnnounces() {
        List<announceDto> announces = announceService.getAnnounceList();
        return SuccessResponse.makeResponse(SuccessStatusCode.ANNOUNCE_GET_SUCCESS, announces);
    }

    @GetMapping("/{announceId}")
    public SuccessResponse<announceDetailDto> getAnnounceDetail(@PathVariable("announceId") Long announceId) {
        announceDetailDto dto = announceService.getAnnounceDetail(announceId);
        if (dto == null) {
            // 적절한 실패 응답 처리 (예: 공고를 찾을 수 없다는 메시지)
            throw new BadRequestException(BadStatusCode.ANNOUNCE_NOT_FOUND);
        }
        return SuccessResponse.makeResponse(SuccessStatusCode.ANNOUNCE_GET_SUCCESS,dto);
    }

}
