package com.springboot.domesticworkregistry.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.springboot.domesticworkregistry.entities.User;
import com.springboot.domesticworkregistry.enums.Role;

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


        // 🔹 Multi-role handling (EMPLOYER + EMPLOYEE)
        if (user.getRoles().contains(Role.EMPLOYER) && user.getRoles().contains(Role.EMPLOYEE)) {
            // Either redirect to unified dashboard (tabs show both roles)…
            getRedirectStrategy().sendRedirect(request, response, "/dashboard");
            return;
            // …or if you want them to explicitly choose a role:
            // getRedirectStrategy().sendRedirect(request, response, "/choose-role");
        }

        // 🔹 Single role handling
        if (user.getRoles().contains(Role.EMPLOYER) ||
                user.getRoles().contains(Role.EMPLOYEE)) {
            // Both roles now share the same dashboard with tabs
            getRedirectStrategy().sendRedirect(request, response, "/dashboard");
        } else if (user.getRoles().contains(Role.ADMIN)) {
            getRedirectStrategy().sendRedirect(request, response, "/admin/dashboard");
        } else {
            // Fallback → landing page
            getRedirectStrategy().sendRedirect(request, response, "/");
        }
    }
}
