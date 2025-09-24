package com.springboot.domesticworkregistry.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.domesticworkregistry.entities.User;

@Controller
@RequestMapping("/users")
public class UserController {
    @GetMapping("/dashboard")
    public String showDashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "dashboard/dashboard";
    }
}
