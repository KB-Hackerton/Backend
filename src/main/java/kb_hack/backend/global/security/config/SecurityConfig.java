package kb_hack.backend.global.security.config;

import kb_hack.backend.global.security.filter.AuthenticationErrorFilter;
import kb_hack.backend.global.security.filter.JwtAuthenticationFilter;
import kb_hack.backend.global.security.filter.JwtUsernamePasswordAuthenticationFilter;
import kb_hack.backend.global.util.JwtProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationErrorFilter authenticationErrorFilter;

    @Bean
    public CharacterEncodingFilter encodingFilter() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        return encodingFilter;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http, JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter) throws Exception {
        http
                .csrf((auth)->auth.disable())
                .formLogin((auth)-> auth.disable())
                .httpBasic((auth)->auth.disable())
                .cors((cors)->{})
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authorizeHttpRequests((auth)-> auth
                        //스웨거,Welcome page 허용
                        .requestMatchers("/assets/**", "/favicon.ico", "/swagger-resources/**", "/swagger-ui.html", "/swagger-ui/**",
                                "/webjars/**", "/swagger/**","/", "/index.html","/api-docs/**","/images/logo.png").permitAll()

                        //실제 permitall 할 ul
                        .requestMatchers("/auth/login","/auth/refresh","/auth/password","/check","/test/*","/email/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/member-info").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/email").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/auth/member-info").hasRole("Member")
                        .requestMatchers(HttpMethod.PATCH,"/auth/member-info").hasRole("Member")
                        .requestMatchers(HttpMethod.DELETE, "/password").hasRole("Member")
                        .requestMatchers(HttpMethod.DELETE, "/home").hasRole("Member")
                        .requestMatchers(HttpMethod.DELETE, "/announce/**").hasRole("Member")
                        .requestMatchers( "/kakao-login/**").permitAll()
                        .requestMatchers("/articles/list").hasRole("Member")
                        .requestMatchers("/notice/*").hasRole("Member")
                        .requestMatchers("/test").hasRole("Member")
                        .requestMatchers("/crawl/admin").hasRole("Admin")
                        .requestMatchers("/admin/ingest/gyeongsang").hasRole("Admin")
                        .requestMatchers("/api/bizinfo").hasRole("Member")
                        .requestMatchers("/favorites/**").hasRole("Member")
                        .requestMatchers("/sos/**").hasRole("Member")
                        .requestMatchers("/profile-image/**").hasRole("Member")
                        .requestMatchers("/connect/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/alarm/admin/**").hasRole("Admin")
                    .anyRequest().authenticated()
                );

        http
                .addFilterBefore(encodingFilter(), CsrfFilter.class)
                .addFilterBefore(authenticationErrorFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());
        return builder.build();
    }



}
