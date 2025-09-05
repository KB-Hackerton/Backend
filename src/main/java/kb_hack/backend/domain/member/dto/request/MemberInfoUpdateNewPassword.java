package kb_hack.backend.domain.member.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberInfoUpdateNewPassword {
    @Schema(description = "기존 비밀번호", example = "12345")
    private String originalPassword;
    @Schema(description = "새로운 비밀번호", example = "12345")
    private String newPassword;

}
