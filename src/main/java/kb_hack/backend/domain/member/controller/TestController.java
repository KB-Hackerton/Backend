package kb_hack.backend.domain.member.controller;

import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.BadRequestException;
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

    @GetMapping("/400")
    public String test400(@RequestParam(required = false) String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException(BadStatusCode.INVALID_PARAMETER_EXCEPTION);
        }
        return "ok";
    }

    @GetMapping("/500")
    public String test500() {
        int error = 1 / 0;
        return "ok";
    }

    @GetMapping("/missing-param")
    public String testMissing(@RequestParam String name) {
        return "hello " + name;
    }

}
