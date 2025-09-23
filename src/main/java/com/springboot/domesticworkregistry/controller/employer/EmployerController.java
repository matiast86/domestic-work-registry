package com.springboot.domesticworkregistry.controller.employer;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.domesticworkregistry.dto.employer.UpdateEmployerDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Employer;
import com.springboot.domesticworkregistry.service.employer.EmployerService;

import jakarta.validation.Valid;

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

    @GetMapping("/updateEmployer")
    public String updateEmployer(@RequestParam("employerId") String id, Model theModel) {
        UpdateEmployerDto form = employerService.getEmployerDto(id);

        theModel.addAttribute("employer", form);

        return "employers/employer-form";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("employer") UpdateEmployerDto form, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employer", form);
            return "employers/employer-form";
        }
        model.addAttribute("employer", form);
        System.out.println("Employer id: " + form.getEmployerId());

        employerService.update(form, form.getEmployerId());

        return "redirect: employers/dashboard";
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
