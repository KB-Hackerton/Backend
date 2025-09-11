package kb_hack.backend.global.webclient;

import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
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
    @Bean
    @Qualifier("bizinfoWebClient")
    public WebClient bizinfoWebClient(){
        return WebClient.builder()
                .baseUrl("https://www.bizinfo.go.kr")
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer ->
                                configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) // 10MB
                        )
                        .build()
                )
                .build();
    }

    @Bean(name = "discordWebClient")
    public WebClient discordWebClient() {
        return WebClient.builder()
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean
    @Qualifier("kakaoAuthWebClient")
    public WebClient kakaoAuthWebClient(){
        return WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .build();
    }


    @Bean
    @Qualifier("kakaoApiWebClient")
    public WebClient kakaoApiWebClient() {
        return WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .build();
    }
    @Bean
    @Qualifier("openAiWebClient")
    public WebClient openAiWebClient(@Value("${openai.api.key}") String openAiKey) {
        return WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + openAiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();

    }
}
