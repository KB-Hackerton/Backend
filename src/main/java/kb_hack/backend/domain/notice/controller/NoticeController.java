package kb_hack.backend.domain.notice.controller;

import kb_hack.backend.domain.notice.dto.response.NoticeDTO;
import kb_hack.backend.domain.notice.service.NoticeService;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;


    @GetMapping("")
    public SuccessResponse<List<NoticeDTO>> getAllNotice() {
        return SuccessResponse.makeResponse(SuccessStatusCode.NOTICE_GET_SUCCESS, noticeService.getAllnotice());
    }

}
