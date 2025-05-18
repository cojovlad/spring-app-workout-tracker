package com.example.spring_app_workout_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Configuration class for Remember-Me authentication.
 * <p>
 * This class sets up the infrastructure to support persistent login (Remember-Me) using Spring Security.
 * It uses JDBC to persist tokens in the database.
 */
@Configuration
public class RememberMeConfig {

    private final DataSource dataSource;

    /**
     * Constructs the RememberMeConfig with the specified DataSource.
     *
     * @param dataSource the {@link DataSource} used to persist Remember-Me tokens
     */
    public RememberMeConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Configures a {@link PersistentTokenRepository} backed by JDBC.
     * <p>
     * This bean is responsible for storing and retrieving persistent tokens from the database.
     * The default table name expected is {@code persistent_logins}.
     *
     * @return a configured {@link JdbcTokenRepositoryImpl} instance
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        
        return tokenRepository;
    }

    /**
     * Generates a random Remember-Me key to be used for signing Remember-Me cookies.
     * <p>
     * This key should be kept consistent across application restarts for persistent logins to remain valid.
     *
     * @return a cryptographically strong random key as a {@link String}
     */
    @Bean
    public String rememberMeKey() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }
}
