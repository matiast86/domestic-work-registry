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

        // 🔹 First login → force password change
        if (user.isFirstLogin()) {
            getRedirectStrategy().sendRedirect(request, response, "/change-password");
            return;
        }

        // 🔹 Handle multiple roles
        if (user.getRoles().contains(Role.EMPLOYER) && user.getRoles().contains(Role.EMPLOYEE)) {
            // Redirect to a "role chooser" page
            getRedirectStrategy().sendRedirect(request, response, "/choose-role");
            return;
        }

        // 🔹 Single role handling
        if (user.getRoles().contains(Role.EMPLOYER)) {
            getRedirectStrategy().sendRedirect(request, response, "/employers/dashboard");
        } else if (user.getRoles().contains(Role.EMPLOYEE)) {
            getRedirectStrategy().sendRedirect(request, response, "/employees/dashboard");
        } else if (user.getRoles().contains(Role.ADMIN)) {
            getRedirectStrategy().sendRedirect(request, response, "/admin/dashboard");
        } else {
            getRedirectStrategy().sendRedirect(request, response, "/");
        }
    }
}

