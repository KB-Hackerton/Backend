package kb_hack.backend.domain.email.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    public void sendVerificationCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        SecurityCustomUser customUser = (SecurityCustomUser) authentication.getPrincipal();
        MemberVO vo = customUser.getMemberVO();
        System.out.println("✨✨✨✨✨✨✨✨✨vo = " + vo);

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(new InternetAddress("zibi_official@naver.com", "경상났네 운영팀"));
            System.out.println("✨✨✨✨✨✨✨✨✨vo.getMemberEmail() = " + vo.getMemberEmail());
            helper.setTo(vo.getMemberEmail());
            helper.setSubject("[경상났네] 이메일 인증번호 안내");

            Context context = new Context();
            String verificationCode = generateCode();
            context.setVariable("code", verificationCode);


            String htmlContent = templateEngine.process("email/verification", context);

            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("인증 메일 전송 실패", e);
        }
    }

    public String generateCode() {
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return uuid.substring(0, 6);
    }


}
