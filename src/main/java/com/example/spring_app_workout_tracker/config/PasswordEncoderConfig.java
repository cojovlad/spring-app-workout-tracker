package com.example.spring_app_workout_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for creating a {@link PasswordEncoder} bean.
 *
 * <p>This class provides a password encoder that delegates to various available password
 * encoding algorithms such as {@code bcrypt}, {@code PBKDF2}, and others. It is a flexible
 * approach that allows you to switch between encoding strategies by using the
 * {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()} method.
 * The delegating password encoder is particularly useful when handling existing user data
 * with different password encoding schemes, or when you want to support multiple encoding algorithms.</p>
 *
 * <p>By default, it uses the {@code PBKDF2PasswordEncoder} with the ability to delegate
 * to other encoders, providing backward compatibility with older encoding schemes (e.g., bcrypt, scrypt).</p>
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * Creates and provides a {@link PasswordEncoder} bean using the
     * {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()} method.
     *
     * <p>This encoder is a flexible solution that supports multiple password encoding
     * strategies. It allows switching encoding algorithms while maintaining backward
     * compatibility with existing hashed passwords.</p>
     *
     * @return a {@link PasswordEncoder} instance configured to delegate between encoding strategies.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
