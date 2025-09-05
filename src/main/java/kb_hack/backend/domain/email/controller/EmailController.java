package kb_hack.backend.domain.email.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.email.dto.request.MailDTO;
import kb_hack.backend.domain.email.dto.request.MailVerificationDTO;
import kb_hack.backend.domain.email.service.EmailService;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Tag(name = "이메일 인증", description = "이메일 인증번호 발송 및 검증 API")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("")
    @Operation(
            summary = "이메일 인증번호 발송",
            description = "입력받은 이메일 주소로 6자리 인증번호를 발송합니다. 인증번호는 Redis에 저장되며 5분간 유효합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이메일 발송 성공",
                            content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
                    @ApiResponse(responseCode = "500", description = "메일 서버 오류 / Redis 저장 오류")
            }
    )
    public SuccessResponse<Void>sendEmail(@RequestBody MailDTO mailDTO){
        emailService.sendVerificationCode(mailDTO.getEmail());
        return SuccessResponse.makeResponse(SuccessStatusCode.EMAIL_SEND_SUCCESS);
    }

    @PostMapping("/verification")
    @Operation(
            summary = "이메일 인증번호 검증",
            description = "사용자가 입력한 인증번호를 Redis에 저장된 값과 비교하여 일치하면 검증 성공으로 처리합니다. 성공 시 Redis에서 해당 키는 삭제됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이메일 인증 성공",
                            content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
                    @ApiResponse(responseCode = "400", description = "인증번호 불일치 / 만료됨 / 이메일 유효하지 않음"),
                    @ApiResponse(responseCode = "500", description = "Redis 조회/삭제 중 오류")
            }
    )
    public SuccessResponse<Void> verificationEmail(@RequestBody MailVerificationDTO mailVerificationDTO){
        emailService.verifyCode(mailVerificationDTO.getEmail(),mailVerificationDTO.getVerificationCode());
        return SuccessResponse.makeResponse(SuccessStatusCode.EMAIL_VERIFY_CODE_SUCCESS);
    }
}
