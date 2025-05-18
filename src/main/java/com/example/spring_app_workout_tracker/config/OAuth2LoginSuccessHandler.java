package com.example.spring_app_workout_tracker.config;

import com.example.spring_app_workout_tracker.entity.Language;
import com.example.spring_app_workout_tracker.entity.Role;
import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.exception.LanguageNotFoundException;
import com.example.spring_app_workout_tracker.exception.RoleNotFoundException;
import com.example.spring_app_workout_tracker.repository.LanguageRepository;
import com.example.spring_app_workout_tracker.repository.RoleRepository;
import com.example.spring_app_workout_tracker.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static com.example.spring_app_workout_tracker.util.MessageKeys.*;

/**
 * Custom success handler for OAuth2 login.
 *
 * <p>This handler performs post-login processing, including:
 * <ul>
 *     <li>Retrieving or registering the user from OAuth2 attributes</li>
 *     <li>Assigning a default role and preferred language if new</li>
 *     <li>Setting the Spring Security context</li>
 *     <li>Redirecting to the dashboard or configured landing page</li>
 * </ul>
 */
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;
    private final RoleRepository roleRepository;

    /**
     * Constructs a new OAuth2LoginSuccessHandler.
     *
     * @param userRepository       Repository for managing User entities
     * @param languageRepository   Repository for managing Language entities
     * @param roleRepository       Repository for managing Role entities
     */
    @Autowired
    public OAuth2LoginSuccessHandler(UserRepository userRepository,
                                     LanguageRepository languageRepository,
                                     RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.languageRepository = languageRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Handles successful OAuth2 authentication.
     *
     * <p>If the user does not exist in the system, a new user is created with default role and language.
     * Then the user's authentication is set in the security context, and they are redirected to the dashboard.</p>
     *
     * @param request        The HTTP request
     * @param response       The HTTP response
     * @param authentication The authentication object
     * @throws IOException      If an input or output error is detected
     * @throws ServletException If the request could not be handled
     */
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        final String email = (String) Optional.ofNullable(oAuth2User.getAttribute("email")).orElse("");
        final String firstName = (String) Optional.ofNullable(oAuth2User.getAttribute("given_name")).orElse("");
        final String lastName = (String) Optional.ofNullable(oAuth2User.getAttribute("family_name")).orElse("");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(StringUtils.hasText(email) ? email : "");
            user.setPasswordHash("");
            user.setEmailVerified(true);

            if (user.getRoles().isEmpty()) {
                Role defaultRole = roleRepository.findById(DEFAULT_ROLE_ID)
                        .orElseThrow(() -> new RoleNotFoundException(DEFAULT_ROLE_ID));
                user.getRoles().add(defaultRole);
            }

            if (user.getPreferredLanguage() == null) {
                Language defaultLanguage = languageRepository.findByCode(DEFAULT_LANGUAGE_CODE)
                        .orElseThrow(() -> new LanguageNotFoundException(DEFAULT_LANGUAGE_CODE));
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