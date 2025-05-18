package com.example.spring_app_workout_tracker.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Configuration class for setting up message source for internationalization (i18n)
 * and validation messages in the Spring application.
 *
 * <p>This configuration loads message resource bundles from the classpath with support
 * for reloading, UTF-8 encoding, and fallback control.
 */
@Configuration
public class MessageConfig {

    /**
     * Configures a {@link MessageSource} bean for resolving messages from resource bundles.
     *
     * <p>This bean:
     * <ul>
     *     <li>Loads messages from {@code classpath:messages.properties} and {@code classpath:ValidationMessages.properties}</li>
     *     <li>Uses UTF-8 encoding</li>
     *     <li>Disables fallback to the system locale</li>
     *     <li>Reloads message files on every request (ideal for development; use a positive cache duration in production)</li>
     * </ul>
     *
     * @return a configured {@link MessageSource} for i18n and validation
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();

        messageSource.setBasenames(
                "classpath:messages",
                "classpath:ValidationMessages"
        );

        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(0);
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }
}