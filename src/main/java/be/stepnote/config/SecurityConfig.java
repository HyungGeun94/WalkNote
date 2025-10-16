package be.stepnote.config;

import be.stepnote.config.security.JWTFilter;
import be.stepnote.config.security.JWTUtil;
import be.stepnote.config.security.LoginFilter;
import be.stepnote.config.security.CustomOAuth2UserService;
import be.stepnote.config.security.CustomSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // id,pw기반 로그인할 때 쓰이는 부분.
    private final AuthenticationConfiguration authenticationConfiguration;
    //

    private final JWTUtil jwtUtil;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final CustomSuccessHandler customSuccessHandler;


    // id,pw기반 로그인할 때 쓰이는 부분.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors((corsCustomizer ->
                corsCustomizer.configurationSource(new CorsConfigurationSource() {

                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOriginPatterns(Arrays.asList(
                        "http://localhost:*",      // React 개발 서버
                        "http://10.0.2.2:*",       // Android 에뮬레이터
                        "http://192.168.*:*",      // 실제 기기에서 접근할 때
                        "https://*"                // 배포 환경 (테스트용으로만)
                    ));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                }
            })));

        //csrf disable
        http
            .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
            .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
            .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/", "/join","/test","/userTest","/ws/**","/ws-info"
                ,"/login/**", "/oauth2/**","/error","/favicon.ico", "/apple-touch-icon.png", "/default-ui.css",
                    "/css/**", "/js/**", "/images/**")
                .permitAll()

                .requestMatchers("/admin").hasRole("ADMIN")

                .anyRequest().authenticated());

        //oauth2
        http
            .oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                    .userService(customOAuth2UserService))
                     .successHandler(customSuccessHandler));

        //JWTFilter 등록
        http
            .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);


        // id/pw기반 로그인할 때 쓰이는 부분
        // 필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        // http
        //    .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }
}