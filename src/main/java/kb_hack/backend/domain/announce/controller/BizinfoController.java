package kb_hack.backend.domain.announce.controller;

import kb_hack.backend.domain.announce.service.BizinfoApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BizinfoController {
    private  final BizinfoApiService bizinfoApiService;
    @GetMapping("/bizinfo")
    public ResponseEntity<String> fetchAndSaveBizinfo() throws Exception{
        int savedCount =bizinfoApiService.fetchAndSaveBizinfo();
        return ResponseEntity.ok(savedCount + "");
    }
}
