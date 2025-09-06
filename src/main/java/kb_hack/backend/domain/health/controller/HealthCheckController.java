package kb_hack.backend.domain.health.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/check")
@RequiredArgsConstructor
@Tag(name = "Checklist", description = "서버 health check")
public class HealthCheckController {

    @Operation(summary = "헬스 체크 ", description = "서버 정상 동작 여부를 확인합니다. 인증 불필요.")
    @GetMapping("")
    public SuccessResponse<Void> test() {
        return SuccessResponse.makeResponse(SuccessStatusCode.HEALTH_CHECK_SUCCESS);
    }
}
