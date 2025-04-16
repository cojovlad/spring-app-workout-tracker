package com.example.spring_app_workout_tracker.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();

        messageSource.setBasenames(
                "classpath:messages",
                "classpath:ValidationMessages"
        );

        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}