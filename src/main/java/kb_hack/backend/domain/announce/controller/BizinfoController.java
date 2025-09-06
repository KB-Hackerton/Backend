package kb_hack.backend.domain.announce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.announce.service.BizinfoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Bizinfo", description = "기업마당 공고 수집 API (관리자용)")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BizinfoController {

    private final BizinfoApiService bizinfoApiService;

    @Operation(
            summary = "기업마당 공고 수집",
            description = "기업마당(Bizinfo) API에서 공고를 가져와 DB에 저장합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/bizinfo")
    public ResponseEntity<String> fetchAndSaveBizinfo() throws Exception {
        int savedCount = bizinfoApiService.fetchAndSaveBizinfo();
        return ResponseEntity.ok(savedCount + "");
    }
}
