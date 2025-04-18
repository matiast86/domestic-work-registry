package com.springboot.domesticworkregistry.controller.employee;

import java.util.List;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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

import com.springboot.domesticworkregistry.dto.address.CreateAddressDto;
import com.springboot.domesticworkregistry.dto.employee.CreateEmployeeDto;
import com.springboot.domesticworkregistry.dto.employee.CreateEmployeeWithAddressDto;
import com.springboot.domesticworkregistry.entities.Employee;
import com.springboot.domesticworkregistry.service.employee.EmployeeService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/")
    public String listEmployees(Model theModel) {
        List<Employee> employees = this.employeeService.findAll();

        theModel.addAttribute("employees", employees);
        return "employees/list-employees";
    }

    @GetMapping("/addEmployee")
    public String addEmployee(Model theModel) {

        theModel.addAttribute("employeeForm", new CreateEmployeeWithAddressDto());
        return "employees/employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(@RequestParam("employerId") String id,
            @Valid @ModelAttribute("employeeForm") CreateEmployeeWithAddressDto form,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeForm", form);
            return "employees/employee-form";
        }
        model.addAttribute("employeeForm", form);

        try {

            employeeService.save(id, form);
        } catch (Exception e) {
            model.addAttribute("employeeForm", form);
            throw e;
        }

        return "redirect:/employee/";
    }

    @GetMapping("/updateEmployee")
    public String updateEmployee(@RequestParam("employeeId") String id, Model theModel) {

        Employee employee = employeeService.findById(id);

        theModel.addAttribute("employee", employee);

        return "employee/employee-form";
    }

}
