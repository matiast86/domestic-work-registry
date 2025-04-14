package com.springboot.domesticworkregistry.controller.employee;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springboot.domesticworkregistry.entities.Employee;
import com.springboot.domesticworkregistry.service.employee.EmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String listEmployees(Model theModel) {
        List<Employee> employees = this.employeeService.findAll();

        theModel.addAttribute("employees", employees);
        return "employees/list-employees";
    }

    @GetMapping("/addEmployee")
    public String addEmployee(Model theModel) {
        Employee employee = new Employee();

        theModel.addAttribute("employee", employee);
        return "employees/employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {

        employeeService.save(employee);
        return "redirect:/employee";
    }



}
