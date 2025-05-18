package com.example.spring_app_workout_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

/**
 * Configuration class for setting up internationalization (i18n) in the application.
 * <p>
 * Configures locale resolution using the session and provides an interceptor
 * to detect locale changes via request parameters.
 * </p>
 */
@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    /**
     * Creates a session-based locale resolver with English as the default locale.
     * <p>
     * The resolved locale is stored in the user's session and will be used
     * for message source resolution throughout the application.
     * </p>
     *
     * @return A configured {@link SessionLocaleResolver} instance
     * @see SessionLocaleResolver
     */
    @Bean
    public SessionLocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    /**
     * Creates an interceptor that detects locale changes via the "lang" request parameter.
     * <p>
     * Example usage: {@code ?lang=fr} in any request will switch to French locale.
     * The interceptor requires a corresponding locale resolver to be configured.
     * </p>
     *
     * @return A configured {@link LocaleChangeInterceptor} instance
     * @see LocaleChangeInterceptor
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * Registers the locale change interceptor with Spring MVC.
     *
     * @param registry The interceptor registry to which the interceptor will be added
     * @see InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}