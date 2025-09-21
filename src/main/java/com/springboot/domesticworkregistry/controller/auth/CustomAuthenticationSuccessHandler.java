package com.springboot.domesticworkregistry.controller.auth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;

import com.springboot.domesticworkregistry.entities.Employer;
import com.springboot.domesticworkregistry.service.employer.EmployerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private EmployerService employerService;

    public CustomAuthenticationSuccessHandler(EmployerService employerService) {
        this.employerService = employerService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
                String userName = authentication.getName();
                Employer employer = employerService.findByEmail(userName);

                HttpSession session = request.getSession();
                session.setAttribute("employer", employer);

                response.sendRedirect(request.getContextPath() + "/employers/dashboard");
            
    }

}
