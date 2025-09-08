package com.springboot.domesticworkregistry.service.employee;

import java.util.List;

import com.springboot.domesticworkregistry.dto.employee.CreateEmployeeWithAddressDto;
import com.springboot.domesticworkregistry.entities.Employee;

public interface EmployeeService {

    List<Employee> findAll();

    Employee findById(String id);

    Employee findByEmail(String email);

    Employee findByCuil(String cuil);

    Employee save(Employee theEmployee);

    Employee save(CreateEmployeeWithAddressDto form);

    void delete(String id);

}
