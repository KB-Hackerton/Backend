package kb_hack.backend.domain.announce.controller;


import kb_hack.backend.domain.announce.dto.announceDto;
import kb_hack.backend.domain.announce.service.announceService;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
