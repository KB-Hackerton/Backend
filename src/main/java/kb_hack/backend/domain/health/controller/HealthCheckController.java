package kb_hack.backend.domain.health.controller;

import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
@RequiredArgsConstructor
public class HealthCheckController {
    @GetMapping("")
    public SuccessResponse<Void> test() { return SuccessResponse.makeResponse(SuccessStatusCode.HEALTH_CHECK_SUCCESS); }
}
