package kb_hack.backend.domain.email.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MailVerificationDTO {
    @Schema(description = "회원 이메일", example = "test@example.com")
    private String email;
    @Schema(description = "발송된 이메일 코드", example = "976CB2")
    private String verificationCode;
}
