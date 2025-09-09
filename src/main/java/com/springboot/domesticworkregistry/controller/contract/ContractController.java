package com.springboot.domesticworkregistry.controller.contract;

import java.util.List;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.service.contract.ContractService;

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


}
