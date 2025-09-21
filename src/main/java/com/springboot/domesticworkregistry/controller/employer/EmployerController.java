package com.springboot.domesticworkregistry.controller.employer;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Employer;
import com.springboot.domesticworkregistry.service.employer.EmployerService;

@Controller
@RequestMapping("/employers")
public class EmployerController {

    private EmployerService employerService;

    public EmployerController(EmployerService employerService) {
        this.employerService = employerService;
    }

    // add mapping for list
    @GetMapping("/")
    public String listEmployers(Model theModel) {
        List<Employer> theEmployers = employerService.findAll();

        theModel.addAttribute("employers", theEmployers);

        return "employers/list-employers";
    }

    @GetMapping("/addEmployer")
    public String addEmployer(Model theModel) {
        Employer theEmployer = new Employer();

        theModel.addAttribute("employer", theEmployer);
        return "employers/employer-form";
    }

    @PostMapping("/save")
    public String saveEmployer(@ModelAttribute("employer") Employer theEmployer) {
        employerService.save(theEmployer);
        return "redirect:/login";
    }

    @GetMapping("/updateEmployer")
    public String updateEmployer(@RequestParam("employerId") String id, Model theModel) {
        Employer theEmployer = employerService.findById(id);

        theModel.addAttribute("employer", theEmployer);

        return "employer/employer-form";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Principal principal, Model model) {
        Employer employer = employerService.findByEmail(principal.getName());
        model.addAttribute("employer", employer);
        return "employers/employer-dashboard";
    }

    @GetMapping("/contracts")
    public String employeesList(@AuthenticationPrincipal Employer employer, Model theModel) {
        List<Contract> contracts = this.employerService.findContractsByEmployer(employer.getId());
        theModel.addAttribute("contracts", contracts);
        return "contracts/list-contracts";
    }

}
