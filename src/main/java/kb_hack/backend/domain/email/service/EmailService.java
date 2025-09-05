package kb_hack.backend.domain.email.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.BadRequestException;
import kb_hack.backend.global.common.exception.type.ServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final StringRedisTemplate redisTemplate;

    public void sendVerificationCode(String email) {
        String verificationCode = generateCode();
        String key = email + ":email-verification";

        try {
            //  Redis에 인증코드 저장 (5분 TTL)
            redisTemplate.opsForValue().set(key, verificationCode, 5, TimeUnit.MINUTES);
        } catch (DataAccessException e) {
            throw new ServerErrorException(BadStatusCode.FAIL_TO_SAVE_VERIFICATION_CODE_REDIS_EXCEPTION);
        }

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(new InternetAddress("zibi_official@naver.com", "경상났네 운영팀"));
            helper.setTo(email);
            helper.setSubject("[경상났네] 이메일 인증번호 안내");

            Context context = new Context();
            context.setVariable("code", verificationCode);

            String htmlContent = templateEngine.process("email/verification", context);
            helper.setText(htmlContent, true);

            emailSender.send(message);

        } catch (MailException e) {
            throw new ServerErrorException(BadStatusCode.FAIL_TO_SEND_MAIL_EXCEPTION);
        } catch (Exception e) {
            throw new ServerErrorException(BadStatusCode.FAIL_TO_SEND_MAIL_EXCEPTION);
        }
    }


    public String generateCode() {
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return uuid.substring(0, 6);
    }


    public void verifyCode(String email, String inputCode) {
        String key = email + ":email-verification";
        try {
            String storedCode = redisTemplate.opsForValue().get(key);

            if (storedCode == null) {
                throw new BadRequestException(BadStatusCode.INVAID_EMAIL_EXCEPTION);
            }
            if (storedCode.equals(inputCode)) {
                redisTemplate.delete(key);
            }else{
                throw new BadRequestException(BadStatusCode.INSUFFICIENT_EMAIL_VERIFICATION_CODE);
            }

        } catch (DataAccessException e) {
            throw new ServerErrorException(BadStatusCode.FAIL_TO_HANDLE_VERIFICATION_CODE_REDIS_EXCEPTION);
        }
    }
}
