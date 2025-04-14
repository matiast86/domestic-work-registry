package com.springboot.domesticworkregistry.service.employee;

import java.util.List;

import com.springboot.domesticworkregistry.entities.Employee;

public interface EmployeeService {

    List<Employee> findAll();

    Employee findById(String id);

    Employee findByEmail(String email);

    Employee save(Employee theEmployee);

    void delete(String id);

}
