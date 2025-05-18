package com.example.spring_app_workout_tracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * Security configuration class that sets up HTTP security for the application,
 * including form login, OAuth2 login, remember-me functionality, and logout handling.
 */
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;
    private final String rememberMeKey;
    private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;

    /**
     * Constructs a new {@code SecurityConfig} with required dependencies.
     *
     * @param userDetailsService         the service to load user-specific data
     * @param persistentTokenRepository  the token repository for remember-me functionality
     * @param rememberMeKey              the secret key used to secure remember-me tokens
     * @param oauth2LoginSuccessHandler  the custom success handler for OAuth2 logins
     */
    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
                          PersistentTokenRepository persistentTokenRepository,
                          String rememberMeKey,
                          OAuth2LoginSuccessHandler oauth2LoginSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.persistentTokenRepository = persistentTokenRepository;
        this.rememberMeKey = rememberMeKey;
        this.oauth2LoginSuccessHandler = oauth2LoginSuccessHandler;
    }

    /**
     * Configures the Spring Security filter chain, including:
     * - Public and protected endpoints
     * - Form-based and OAuth2 login
     * - Remember-me authentication
     * - Logout behavior
     * - CSRF protection (disabled)
     *
     * @param http the {@link HttpSecurity} to modify
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/error",
                                "/api/v1/auth/login",
                                "/api/v1/auth/register",
                                "/css/**",
                                "/js/**",
                                "/favicon.ico",
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/api/v1/profile/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/api/v1/auth/login")
                        .loginProcessingUrl("/api/v1/auth/login")
                        .defaultSuccessUrl("/api/v1/dashboard", true)
                        .failureUrl("/api/v1/auth/login?error=true")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/api/v1/auth/login")
                        .defaultSuccessUrl("/api/v1/dashboard", true)
                        .successHandler(oauth2LoginSuccessHandler)
                )
                .rememberMe(rememberMe -> rememberMe
                        .tokenRepository(persistentTokenRepository)
                        .userDetailsService(userDetailsService)
                        .key(rememberMeKey)
                        .tokenValiditySeconds(7 * 24 * 60 * 60) // 7 days
                        .rememberMeParameter("remember-me")
                        .rememberMeCookieName("remember-me-cookie")
                        .useSecureCookie(true)
                        .alwaysRemember(true)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessUrl("/api/v1/auth/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me-cookie")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
