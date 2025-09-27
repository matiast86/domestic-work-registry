package com.springboot.domesticworkregistry.controller.dashboard;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.domesticworkregistry.entities.User;
import com.springboot.domesticworkregistry.service.contract.ContractService;
import com.springboot.domesticworkregistry.service.user.UserService;

@Controller
@RequestMapping("/dashboard")
@PreAuthorize("isAuthenticated()") // all endpoints require login
public class DashboardController {

    private final ContractService contractService;
    private final UserService userService;

    public DashboardController(ContractService contractService, UserService userService) {
        this.contractService = contractService;
        this.userService = userService;
    }

    // Main dashboard page
    @GetMapping
    public String dashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "dashboard/dashboard";
    }

    // Employer contracts fragment
    @GetMapping("/employer-contracts")
    @PreAuthorize("hasRole('EMPLOYER')") // only employers see this tab
    public String employerContracts(@AuthenticationPrincipal User employer,
            Model model,
            @RequestParam(name = "standalone", defaultValue = "false") boolean standalone) {
        model.addAttribute("contracts", contractService.findAllByEmployer(employer.getId()));

        if (standalone) {
            return "contracts/employer-contracts-page"; // full page with layout
        }
        return "dashboard/fragment :: employerContracts"; // fragment only
    }

    // Employee contracts fragment
    @GetMapping("/employee-contracts")
    @PreAuthorize("hasRole('EMPLOYEE')") // only employees see this tab
    public String employeeContracts(@AuthenticationPrincipal User employee,
            Model model,
            @RequestParam(name = "standalone", defaultValue = "false") boolean standalone) {
        model.addAttribute("contracts", userService.findContractsByEmployee(employee.getId()));

        if (standalone) {
            return "contracts/employee-contracts-page";
        }
        return "dashboard/fragment :: employeeContracts";
    }
}
