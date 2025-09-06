package kb_hack.backend.domain.festival.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.festival.integration.TourIngestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/ingest")
@Tag(name = "AdminIngest", description = "경상권 축제 서버에 넣는 API (관리자용) ")
@Slf4j
public class AdminIngestController {

    private final TourIngestService ingestService;

    @Operation(
            summary = "경상권 축제 데이터 강제 수집",
            description = "TourAPI로부터 경상권 축제 데이터를 수집/업데이트합니다. (관리자 전용)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/gyeongsang")
    public Mono<ResponseEntity<String>> runOnce() {
        return ingestService.ingestGyeongsang()
                .map(n -> ResponseEntity.ok("upserted rows: " + n))
                .onErrorResume(e -> {
                    log.error("ingest failed", e);
                    String msg = e.getClass().getSimpleName() + ": " +
                            (e.getMessage() == null ? "(no message)" : e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body("INGEST ERROR: " + msg));
                });
    }
}
