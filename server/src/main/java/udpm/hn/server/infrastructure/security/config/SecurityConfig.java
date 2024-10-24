package udpm.hn.server.infrastructure.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.infrastructure.constant.Role;
import udpm.hn.server.infrastructure.security.exception.RestAuthenticationEntryPoint;
import udpm.hn.server.infrastructure.security.filter.TokenAuthenticationFilter;
import udpm.hn.server.infrastructure.security.oauth2.CustomOAuth2UserService;
import udpm.hn.server.infrastructure.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import udpm.hn.server.infrastructure.security.oauth2.OAuth2AuthenticationFailureHandler;
import udpm.hn.server.infrastructure.security.oauth2.OAuth2AuthenticationSuccessHandler;

import java.util.Collections;
import java.util.List;

import static udpm.hn.server.utils.Helper.appendWildcard;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Value("${frontend.url}")
    private String allowedOrigin;

    @Value("${ws.registerEndpoint}")
    private String WEB_SOCKET_ENDPOINT;

    @Value("${ws.applicationPrefix}")
    private String WEB_SOCKET_APP_PREFIX;

    @Value("${ws.topicPrefix}")
    private String WEB_SOCKET_TOPIC_PREFIX;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        source.registerCorsConfiguration("/**", config.applyPermitDefaultValues());
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "*"));
        config.setAllowedOrigins(Collections.singletonList(allowedOrigin));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));
        return source;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(c -> c.configurationSource(corsConfigurationSource()));
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.exceptionHandling(e -> e.authenticationEntryPoint(new RestAuthenticationEntryPoint()));
        // Public APIs
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                                appendWildcard(MappingConstants.API_AUTH_PREFIX),
                                appendWildcard(MappingConstants.PATH_OAUTH2),
                                appendWildcard(MappingConstants.VERSION)
                        )
                        .permitAll()
        );
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                                appendWildcard(MappingConstants.API_COMMON),
                                appendWildcard(MappingConstants.API_NOTIFICATION),
                                appendWildcard(MappingConstants.API_LOG)
                        )
                        .hasAnyAuthority(
                                Role.ADMIN.name(),
                                Role.CHU_NHIEM_BO_MON.name(),
                                Role.GIANG_VIEN.name(),
                                Role.TRUONG_MON.name(),
                                Role.NGUOI_LAP_KE_HOACH.name(),
                                Role.SUPER_ADMIN.name(),
                                Role.SINH_VIEN.name()
                        )
        );
        // Admin APIs
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                                appendWildcard(MappingConstants.API_ADMIN_DEPARTMENT),
                                appendWildcard(MappingConstants.API_ADMIN_SUBJECT),
                                appendWildcard(MappingConstants.API_ADMIN_MAJOR),
                                appendWildcard(MappingConstants.API_ADMIN_SEMESTER),
                                appendWildcard(MappingConstants.API_ADMIN_BLOCK),
                                appendWildcard(MappingConstants.API_ADMIN_MAJOR_FACILITY),
                                appendWildcard(MappingConstants.API_ADMIN_DEPARTMENT_FACILITY),
                                appendWildcard(MappingConstants.API_ADMIN_STAFF),
                                appendWildcard(MappingConstants.API_ADMIN_ROLE),
                                appendWildcard(MappingConstants.API_SUPER_ADMIN_FACILITY)
                        )
                        .hasAnyAuthority(Role.ADMIN.name())
        );
        // Head of Department APIs
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                                appendWildcard(MappingConstants.API_HEAD_DEPARTMENT_HEAD_PLAN),
                                appendWildcard(MappingConstants.API_HEAD_DEPARTMENT_HEAD_SUBJECT),
                                appendWildcard(MappingConstants.API_PLAN_LOG_HISTORY),
                                appendWildcard(MappingConstants.API_HEAD_DEPARTMENT_PLANNER)
                        )
                        .hasAnyAuthority(Role.CHU_NHIEM_BO_MON.name())
        );
        // Teacher APIs
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                                appendWildcard(MappingConstants.API_TEACHER_TUTOR_CLASS),
                                appendWildcard(MappingConstants.API_TEACHER_STUDENT_ATTENDANCE)
                        )
                        .hasAnyAuthority(Role.GIANG_VIEN.name())
        );
        // Head of Subject APIs
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                                appendWildcard(MappingConstants.API_HEAD_SUBJECT_PLAN),
                                appendWildcard(MappingConstants.API_HEAD_SUBJECT_TUTOR_DETAIL),
                                appendWildcard(MappingConstants.API_PLAN_LOG_HISTORY)
                        )
                        .hasAnyAuthority(Role.TRUONG_MON.name())
        );
        // Planner APIs
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                                appendWildcard(MappingConstants.API_PLANNER_PLAN),
                                appendWildcard(MappingConstants.API_PLAN_LOG_HISTORY)
                        )
                        .hasAnyAuthority(Role.NGUOI_LAP_KE_HOACH.name())
        );
        // Super Admin APIs
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                                appendWildcard(MappingConstants.API_SUPER_ADMIN_OPERATION_LOG),
                                appendWildcard(MappingConstants.API_PLAN_LOG_HISTORY),
                                appendWildcard(MappingConstants.API_SUPER_ADMIN_USER_ACTIVITY),
                                appendWildcard(MappingConstants.API_SUPER_ADMIN_DEPARTMENT),
                                appendWildcard(MappingConstants.API_SUPER_ADMIN_FACILITY),
                                appendWildcard(MappingConstants.API_SUPER_ADMIN_MAJOR),
                                appendWildcard(MappingConstants.API_SUPER_ADMIN_DEPARTMENT_FACILITY),
                                appendWildcard(MappingConstants.API_SUPER_ADMIN_MAJOR_FACILITY)
                        )
                        .hasAnyAuthority(Role.SUPER_ADMIN.name())
        );
        // WebSocket
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(
                                appendWildcard(WEB_SOCKET_ENDPOINT),
                                appendWildcard(WEB_SOCKET_APP_PREFIX),
                                appendWildcard(WEB_SOCKET_TOPIC_PREFIX)
                        )
                        .permitAll()
        );
        http.authorizeHttpRequests(auth -> auth.anyRequest().denyAll());
        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.oauth2Login(oauth2 -> oauth2.authorizationEndpoint(
                        a -> a.baseUri(MappingConstants.PATH_OAUTH2 + "/authorize")
                )
                .redirectionEndpoint(
                        r -> r.baseUri(appendWildcard(MappingConstants.PATH_OAUTH2 + "/callback"))
                )
                .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                .authorizationEndpoint(a -> a.authorizationRequestRepository(cookieAuthorizationRequestRepository()))
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler));
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
