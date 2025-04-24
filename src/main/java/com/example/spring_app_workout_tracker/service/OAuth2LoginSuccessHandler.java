package com.example.spring_app_workout_tracker.service;

import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;

    @Override
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
            userRepository.save(user);
        }

        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        response.sendRedirect("/api/v1/dashboard");
    }
}