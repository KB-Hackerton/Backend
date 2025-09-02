package kb_hack.backend.domain.member.controller;

import kb_hack.backend.global.security.entity.MemberVO;
import kb_hack.backend.global.security.dto.LoginRequestDTO;
import kb_hack.backend.global.security.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final MemberMapper memberMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping("")
    public String test() { return "ok"; }
}
