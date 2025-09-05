package kb_hack.backend.domain.email.controller;

import kb_hack.backend.domain.email.dto.MailDTO;
import kb_hack.backend.domain.email.service.EmailService;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @GetMapping("")
    public SuccessResponse<Void>sendEmail(){
        emailService.sendVerificationCode();
        return SuccessResponse.makeResponse(SuccessStatusCode.EMAIL_SEND_SUCCESS);
    }
}
