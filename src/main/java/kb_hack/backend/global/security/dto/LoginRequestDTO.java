package kb_hack.backend.global.security.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.servlet.http.HttpServletRequest;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.UnAuthorizedException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Log4j2
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginRequestDTO {
    private String memberEmail;
    private String password;

    public static LoginRequestDTO convert (HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(request.getInputStream(), LoginRequestDTO.class);
        } catch (Exception e) {
            throw new UnAuthorizedException(BadStatusCode.LOGIN_FAILURE_EXCEPTION);
        }
    }

}
