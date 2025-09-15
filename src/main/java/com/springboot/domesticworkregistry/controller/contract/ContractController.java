package com.springboot.domesticworkregistry.controller.contract;

import java.util.List;

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

import com.springboot.domesticworkregistry.dto.contract.ContractDetailsWithemployeeDto;
import com.springboot.domesticworkregistry.dto.contract.CreateEmployeeFormDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Employer;
import com.springboot.domesticworkregistry.service.contract.ContractService;

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
    public String listContractByEmployer(@AuthenticationPrincipal Employer employer, Model theModel) {
        List<Contract> contracts = this.contractService.findAllByEmployer(employer.getId());
        theModel.addAttribute("contracts", contracts);
        return "contracts/list-contracts";
    }

    @PostMapping("/save")
    public String saveContract(@AuthenticationPrincipal Employer employer,
            @Valid @ModelAttribute("employeeForm") CreateEmployeeFormDto form, BindingResult bindingResult,
            Model model) {
        System.out.println("Employer: " + employer.getEmail());

        String employerEmail = employer.getEmail();

        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeForm", form);
            return "employees/employee-form";
        }
        model.addAttribute("employeeForm", form);

        try {

            contractService.save(employerEmail, form);
        } catch (Exception e) {
            model.addAttribute("employeeForm", form);
            throw e;
        }

        return "redirect:/contract/list";

    }

    @GetMapping("/contractForm")
    public String contractForm(Model model) {
        model.addAttribute("contractForm", new CreateEmployeeFormDto());

        return "employees/employee-form";
    }

    @GetMapping("/employee")
    public String employeeInfo(@RequestParam("contractId") String contractIdStr, Model model) {
        int contractId = Integer.parseInt(contractIdStr);
        ContractDetailsWithemployeeDto contract = contractService.findByIdWithEmployee(contractId);
        model.addAttribute("employee", contract);

        return "employees/employee-details";

    }

    @GetMapping("/updateForm")
    public String updateForm(@RequestParam("contractId") int contractId, Model model) {

        ContractDetailsWithemployeeDto contract = contractService.findByIdWithEmployee(contractId);
        model.addAttribute("contractForm", contract);

        return "contracts/contract-update-form";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("contractForm") ContractDetailsWithemployeeDto form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("contractForm", form);
            return "contracts/contract-update-form";
        }
        model.addAttribute("contractForm", form);

        contractService.update(form.getContractId(), form);

        return "redirect:/contract/list";

    }

}
