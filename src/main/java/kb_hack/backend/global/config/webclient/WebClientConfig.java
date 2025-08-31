package kb_hack.backend.global.config.webclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Configuration
public class WebClientConfig {

    @Bean
    @Qualifier("tourApiWebClient")
    public WebClient tourApiWebClient(
            @Value("${tour.api.base-url}") String baseUrl
    ) {
        // 공통 파라미터를 관리하기 위한 설정
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
        // factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        factory.setDefaultUriVariables(Map.of(
                "MobileOS", "ETC",
                "MobileApp", "TestApp",
                "_type", "json" // 응답을 JSON으로 받기 위한 공통 파라미터 추가
        ));

        return WebClient.builder()
                .uriBuilderFactory(factory) // 생성된 factory를 WebClient에 설정
                .baseUrl(baseUrl)
                .build();
    }
}
