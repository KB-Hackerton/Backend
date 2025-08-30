package kb_hack.backend.global.swagger.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My API")
                        .version("1.0")
                        .description("API 문서입니다")
                        .contact(new Contact()
                                .name("개발팀")
                                .email("dev@example.com")))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("개발 서버"),
                        new Server().url("https://api.example.com").description("운영 서버")
                ));
    }
}
