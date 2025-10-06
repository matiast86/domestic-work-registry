package com.springboot.domesticworkregistry.controller.contract;

import java.util.List;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.domesticworkregistry.dto.contract.ContractDetailsWithemployeeDto;
import com.springboot.domesticworkregistry.dto.contract.CreateEmployeeFormDto;
import com.springboot.domesticworkregistry.dto.schedule_entry.ScheduleEntryDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.User;
import com.springboot.domesticworkregistry.service.contract.ContractService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/list")
    public String listContractByEmployer(@AuthenticationPrincipal User employer, Model model) {
        List<Contract> contracts = this.contractService.findAllByEmployer(employer.getId());
        model.addAttribute("contracts", contracts);
        return "contracts/list-contracts";
    }

    @GetMapping("/contractForm")
    public String contractForm(Model model) {
        CreateEmployeeFormDto form = new CreateEmployeeFormDto();
        if (form.getEntries() == null || form.getEntries().isEmpty()) {
            form.setEntries(List.of(new ScheduleEntryDto())); // one blank row
        }
        model.addAttribute("employeeForm", form);
        return "contracts/contract-form";
    }

    @PostMapping("/save")
    public String saveContract(
            @AuthenticationPrincipal User employer,
            @Valid @ModelAttribute("employeeForm") CreateEmployeeFormDto form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeForm", form);
            return "contracts/contract-form";
        }

        contractService.save(employer.getEmail(), form);

        return "redirect:/dashboard";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleDuplicateContract(IllegalStateException ex,
            @ModelAttribute("employeeForm") CreateEmployeeFormDto form,
            Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("employeeForm", form); // repopulate form with user input
        return "contracts/contract-form";
    }

    @GetMapping("/details/{id}")
    private String cotractDetailsForEmployer(@AuthenticationPrincipal User employer, @PathVariable("id") int id,
            Model model) {
        ContractDetailsWithemployeeDto contract = contractService.findByIdWithEmployee(id, employer.getId());
        model.addAttribute("contract", contract);

        return "employees/employee-details";
    }

    @GetMapping("/updateForm/{id}")
    private String updateForm(@AuthenticationPrincipal User employer, @PathVariable("id") int id, Model model) {
        ContractDetailsWithemployeeDto contract = contractService.findByIdWithEmployee(id, employer.getId());
        model.addAttribute("contract", contract);

        return "contracts/contract-update-form";
    }

    @PostMapping("update")
    public String update(@Valid @ModelAttribute("contract") ContractDetailsWithemployeeDto form,
            BindingResult bindingResult,
            Model model) {
        int contractId = form.getContractId();

        if (bindingResult.hasErrors()) {
            model.addAttribute("contract", form);
            return "contracts/contract-update-form";
        }

        contractService.update(contractId, form);

        return "redirect:/contracts/details/" + contractId;
    }
}
