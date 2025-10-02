package com.springboot.domesticworkregistry.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FirstLoginRedirectFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof com.springboot.domesticworkregistry.entities.User user) {

            // Only force redirect if user is flagged as first login
            if (user.isFirstLogin()) {
                String path = request.getServletPath();

                // Allow access to password change endpoints without redirect loop
                if (!path.startsWith("/users/change")
                        && !path.startsWith("/css")
                        && !path.startsWith("/js")) {
                    response.sendRedirect(request.getContextPath() + "/users/changePassword");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

}
