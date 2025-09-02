package kb_hack.backend.global.security.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberAuthVO implements GrantedAuthority {
    String auth;

    @Override
    public String getAuthority() {
        return auth;
    }
}
