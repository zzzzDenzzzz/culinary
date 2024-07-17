package ru.gorohov.culinary_blog.config;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static ru.gorohov.culinary_blog.models.Permission.*;
import static ru.gorohov.culinary_blog.models.Role.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private static final String[] PUBLIC_URLS = {
            "/",
            "/recipe/details/{id}",
            "/register",
            "/register/save",
            "/css/**",
            "/files/**",
            "/error**",
            "/add-comment",
            "/recipeImages/**"
    };

    public static final String RECIPE_ADD_URL = "/recipe/add";
    public static final String RECIPE_EDIT_URL_PREFIX = "/recipe/edit/**";
    private static final String ADMIN_URL_PREFIX = "/admin/**";
    private static final String ADMIN_DELETE_USER_URL_PREFIX = "/admin/delete-user/**";
    private static final String ADMIN_SHOW_USERS_URL = "/admin/show-users";
    private static final String ADMIN_BAN_USER_URL_PREFIX = "/admin/ban-user/**";
    private static final String ADMIN_UNBAN_USER_URL_PREFIX = "/admin/unban-user/{userId}**";
    public static final String FORM_LOGIN = "/login";
    public static final String FORM_LOGIN_ERROR = "/login?error=true";
    public static final String LOGOUT = "/logout";

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        try {
            getAuthorizeHttpRequests(http)
                    .formLogin(SecurityConfig::getHttpSecurityFormLoginConfigurer)
                    .logout(SecurityConfig::getHttpSecurityLogoutConfigurer);

            return http.build();
        }
        catch (Exception e) {
            log.error("Произошла ошибка при конфигурации безопасности. {}", e.getMessage());
            throw e;
        }
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        try {
            auth
                    .userDetailsService(customUserDetailsService)
                    .passwordEncoder(passwordEncoder());
        }
        catch (Exception e) {
            log.error("Произошла ошибка при конфигурации аутентификации. {}", e.getMessage());
            throw e;
        }
    }

    @NotNull
    private static HttpSecurity getAuthorizeHttpRequests(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .requestMatchers(RECIPE_ADD_URL).authenticated()
                        .requestMatchers(RECIPE_EDIT_URL_PREFIX).authenticated()
                        .requestMatchers(ADMIN_URL_PREFIX).hasAnyRole(ADMIN.name())
                        .requestMatchers(ADMIN_DELETE_USER_URL_PREFIX).hasAnyAuthority(ADMIN_DELETE.name())
                        .requestMatchers(ADMIN_SHOW_USERS_URL).hasAnyAuthority(ADMIN_READ.name())
                        .requestMatchers(ADMIN_BAN_USER_URL_PREFIX).hasAnyAuthority(ADMIN_BAN.name())
                        .requestMatchers(ADMIN_UNBAN_USER_URL_PREFIX).hasAnyAuthority(ADMIN_BAN.name())
                        .anyRequest()
                        .authenticated()
                );
    }

    private static void getHttpSecurityFormLoginConfigurer(FormLoginConfigurer<HttpSecurity> form) {
        form
                .loginPage(FORM_LOGIN)
                .defaultSuccessUrl(PUBLIC_URLS[0])
                .failureUrl(FORM_LOGIN_ERROR)
                .permitAll();
    }

    private static void getHttpSecurityLogoutConfigurer(LogoutConfigurer<HttpSecurity> logout) {
        logout
                .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT))
                .logoutSuccessUrl("/")
                .permitAll();
    }
}