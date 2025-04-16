package com.example.spring_app_workout_tracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;
    private final String rememberMeKey;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
                          PersistentTokenRepository persistentTokenRepository,
                          String rememberMeKey) {
        this.userDetailsService = userDetailsService;
        this.persistentTokenRepository = persistentTokenRepository;
        this.rememberMeKey = rememberMeKey;
    }

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
                                "/favicon.ico"
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
