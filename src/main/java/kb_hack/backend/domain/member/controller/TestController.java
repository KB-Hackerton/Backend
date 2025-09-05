package kb_hack.backend.domain.member.controller;

import kb_hack.backend.domain.business.dto.BusinessDTO;
import kb_hack.backend.domain.business.service.BusinessService;
import kb_hack.backend.domain.member.dto.request.SigunUpRequestDTO;
import kb_hack.backend.domain.member.service.MemberService;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.exception.type.BadRequestException;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import kb_hack.backend.global.security.mapper.SecurityMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private  final MemberService memberService;


    @GetMapping("")
    public String test() { return "ok"; }

    @PostMapping("/signup")
    public SuccessResponse<Void> signUpBusiness(@RequestBody SigunUpRequestDTO requestDTO) {
         memberService.signup(requestDTO);
         return SuccessResponse.makeResponse(SuccessStatusCode.SIGNUP_SUCCESS);
    }

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
