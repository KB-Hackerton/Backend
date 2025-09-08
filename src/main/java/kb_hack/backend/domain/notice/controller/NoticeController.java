package kb_hack.backend.domain.notice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "공지사항 API", description = "공지사항 관련 기능을 제공합니다.")
@SecurityRequirement(name = "bearerAuth")  // 🔒 Swagger 자물쇠 (JWT Security 설정)
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(
            summary = "공지사항 전체 조회",
            description = "등록된 모든 공지사항을 최신순으로 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항 조회 성공"),
            @ApiResponse(responseCode = "401", description = "JWT 토큰 인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("")
    public SuccessResponse<List<NoticeDTO>> getAllNotice() {
        return SuccessResponse.makeResponse(
                SuccessStatusCode.NOTICE_GET_SUCCESS,
                noticeService.getAllnotice()
        );
    }
}
