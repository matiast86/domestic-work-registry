package com.springboot.domesticworkregistry.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.springboot.domesticworkregistry.entities.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        User user = (User) authentication.getPrincipal();

        // 🔹 First login → force password change
        if (user.isFirstLogin()) {
            getRedirectStrategy().sendRedirect(request, response, "/change-password");
            return;
        }

        // 🔹 Redirect by role
        String redirectUrl = switch (user.getRole()) {
            case EMPLOYER -> "/employers/dashboard";
            case EMPLOYEE -> "/employees/dashboard";
            default -> "/";
        };

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
