package kb_hack.backend.domain.member.controller;

import kb_hack.backend.global.security.mapper.SecurityMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final SecurityMemberMapper memberMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping("")
    public String test() { return "ok"; }
}
