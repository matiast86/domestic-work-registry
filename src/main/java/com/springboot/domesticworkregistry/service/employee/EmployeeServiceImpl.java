package com.springboot.domesticworkregistry.service.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.EmployeeRepository;
import com.springboot.domesticworkregistry.entities.Employee;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;

    }

    @Override
    public Employee findById(String id) {
        Optional<Employee> result = employeeRepository.findById(id);
        Employee theEmployee = null;

        if(result.isPresent()) {
            theEmployee = result.get();
        } else {
            throw new RuntimeException("Employee not found");
        }

        return theEmployee;
    }

    @Override
    public Employee findByEmail(String email) {
        Optional<Employee> result =  employeeRepository.findByEmail(email);

        Employee theEmployee = null;

        if(result.isPresent()) {
            theEmployee = result.get();
        } else {
            throw new RuntimeException("Employee not found");
        }

        return theEmployee;
    }

    @Override
    public Employee save(Employee theEmployee) {
        return employeeRepository.save(theEmployee);
    }

    @Override
    public void delete(String id) {
        employeeRepository.deleteById(id);
    }

}
