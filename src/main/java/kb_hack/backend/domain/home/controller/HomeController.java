package kb_hack.backend.domain.home.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.home.dto.response.HomeResponse;
import kb_hack.backend.domain.home.service.HomeService;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
@Tag(name = "홈 API", description = "홈 화면에 필요한 데이터를 제공합니다.")
public class HomeController {

    private final HomeService homeService;

    @Operation(
            summary = "홈 데이터 조회",
            description = "홈 화면에 표시될 공지사항, 축제, 기사, 최근 본 공고 데이터를 반환합니다.",
            security =  @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "홈 데이터 조회 성공",
                            content = @Content(
                                    schema = @Schema(implementation = HomeResponse.class)
                            )
                    )
            }
    )
    @GetMapping("")
    public SuccessResponse<HomeResponse> homeResponse() {
        return SuccessResponse.makeResponse(
                SuccessStatusCode.HOME_DATA_LOAD_SUCCESS,
                homeService.getHomeData()
        );
    }
}
