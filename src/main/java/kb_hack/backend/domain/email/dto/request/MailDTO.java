package kb_hack.backend.domain.email.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailDTO {
    @Schema(description = "회원 이메일", example = "test@example.com")
    private String email;
}
