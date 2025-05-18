package com.example.spring_app_workout_tracker.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Configuration class for customizing bean validation.
 * <p>
 * This class defines a {@link LocalValidatorFactoryBean} that integrates with Spring's
 * {@link MessageSource} to provide localized validation messages.
 */
@Configuration
public class ValidationConfig {

    /**
     * Creates and configures a {@link LocalValidatorFactoryBean} with a custom {@link MessageSource}.
     * <p>
     * This allows validation annotations (like {@code @NotNull}, {@code @Size}, etc.)
     * to use messages from properties files (e.g., {@code messages.properties}, {@code messages_en.properties}).
     *
     * @param messageSource the message source used for resolving validation messages
     * @return a validator factory bean that integrates with the provided message source
     */
    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}