package com.example.spring_app_workout_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.security.SecureRandom;

@Configuration
public class RememberMeConfig {

    private final DataSource dataSource;

    public RememberMeConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public String rememberMeKey() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }
}
