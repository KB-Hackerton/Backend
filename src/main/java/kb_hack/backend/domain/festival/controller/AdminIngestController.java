package kb_hack.backend.domain.festival.controller;

import kb_hack.backend.domain.festival.integration.TourIngestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/ingest")
@Slf4j
public class AdminIngestController {
    private final TourIngestService ingestService;
    @GetMapping("/gyeongsang")
    public Mono<ResponseEntity<String>> runOnce() {
        return ingestService.ingestGyeongsang()
                .map(n -> ResponseEntity.ok("upserted rows: " + n))
                .onErrorResume(e -> {
                    log.error("ingest failed", e);
                    String msg = e.getClass().getSimpleName() + ": " + (e.getMessage()==null? "(no message)" : e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().body("INGEST ERROR: " + msg));
                });
    }
}
