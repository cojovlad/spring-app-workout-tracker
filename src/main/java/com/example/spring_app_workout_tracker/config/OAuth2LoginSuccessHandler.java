package com.example.spring_app_workout_tracker.config;

import com.example.spring_app_workout_tracker.entity.Language;
import com.example.spring_app_workout_tracker.entity.Role;
import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.exception.CustomAppException;
import com.example.spring_app_workout_tracker.repository.LanguageRepository;
import com.example.spring_app_workout_tracker.repository.RoleRepository;
import com.example.spring_app_workout_tracker.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;
    private final RoleRepository roleRepository;
    private final MessageSource messageSource;

    @Autowired
    public OAuth2LoginSuccessHandler(UserRepository userRepository, LanguageRepository languageRepository, RoleRepository roleRepository, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.languageRepository = languageRepository;
        this.roleRepository = roleRepository;
        this.messageSource = messageSource;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = (String) Optional.ofNullable(oAuth2User.getAttribute("email")).orElse("");
        String firstName = (String) Optional.ofNullable(oAuth2User.getAttribute("given_name")).orElse("");
        String lastName = (String) Optional.ofNullable(oAuth2User.getAttribute("family_name")).orElse("");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(!email.isEmpty() ? email : "");
            user.setPasswordHash("");
            user.setEmailVerified(true);

            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                Role defaultRole = roleRepository.findById(1L)
                        .orElseThrow(() -> new CustomAppException("error.role.not.found", new Object[]{1L}, HttpStatus.INTERNAL_SERVER_ERROR, "DEFAULT_ROLE_NOT_FOUND", messageSource));
                user.getRoles().add(defaultRole);
            }

            if (user.getPreferredLanguage() == null) {
                Language defaultLanguage = languageRepository.findByCode("en")
                        .orElseThrow(() -> new CustomAppException("error.language.not.found", new Object[]{"en"}, HttpStatus.INTERNAL_SERVER_ERROR, "DEFAULT_LANGUAGE_NOT_FOUND", messageSource));
                user.setPreferredLanguage(defaultLanguage);
            }

            userRepository.save(user);
        }

        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        response.sendRedirect("/api/v1/dashboard");
    }
}