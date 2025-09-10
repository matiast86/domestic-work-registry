package com.springboot.domesticworkregistry.controller.contract;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.domesticworkregistry.dto.contract.CreateContractWithEmployeeDto;
import com.springboot.domesticworkregistry.dto.employee.CreateEmployeeWithAddressDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Employer;
import com.springboot.domesticworkregistry.entities.User;
import com.springboot.domesticworkregistry.service.contract.ContractService;
import com.springboot.domesticworkregistry.service.employer.EmployerDetailsService;
import com.springboot.domesticworkregistry.service.employer.EmployerService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/contract")
public class ContractController {

    private ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/")
    public String listContract(Model theModel) {
        List<Contract> contracts = this.contractService.findAll();
        theModel.addAttribute("contracts", contracts);
        return "employees/list-employees";
    }

    @GetMapping("/list")
    public String listContractByEmployer(@RequestParam("employerId") String employerId, Model theModel) {
        List<Contract> contracts = this.contractService.findAllByEmployer(employerId);
        theModel.addAttribute("contracts", contracts);
        return "employees/list-employees";
    }

    @PostMapping("/save")
    public String saveContract(@AuthenticationPrincipal Employer employer,
            @Valid @ModelAttribute("employeeForm") CreateContractWithEmployeeDto form, BindingResult bindingResult,
            Model model) {
        System.out.println("Employer: " + employer.getEmail());

        String employerEmail = employer.getEmail();

        CreateEmployeeWithAddressDto employeeDto = form.getEmployeeDto();

        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeForm", employeeDto);
            return "employees/employee-form";
        }
        model.addAttribute("employeeForm", employeeDto);

        try {

            contractService.save(employerEmail, form);
        } catch (Exception e) {
            model.addAttribute("employeeForm", employeeDto);
            throw e;
        }

        return "redirect:/contract/list";

    }

}
