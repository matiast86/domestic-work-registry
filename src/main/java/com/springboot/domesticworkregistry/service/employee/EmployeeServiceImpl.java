package com.springboot.domesticworkregistry.service.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.EmployeeRepository;
import com.springboot.domesticworkregistry.dto.employee.CreateEmployeeWithAddressDto;
import com.springboot.domesticworkregistry.entities.Address;
import com.springboot.domesticworkregistry.entities.Employee;
import com.springboot.domesticworkregistry.mapper.AddressMapper;
import com.springboot.domesticworkregistry.mapper.EmployeeMapper;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final AddressMapper addressMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper, AddressMapper addressMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.addressMapper = addressMapper;
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

        if (result.isPresent()) {
            theEmployee = result.get();
        } else {
            throw new RuntimeException("Employee not found");
        }

        return theEmployee;
    }

    @Override
    public Employee findByEmail(String email) {
        Optional<Employee> result = employeeRepository.findByEmail(email);

        Employee theEmployee = null;

        if (result.isPresent()) {
            theEmployee = result.get();
        } else {
            throw new RuntimeException("Employee not found");
        }

        return theEmployee;
    }

    @Override
    public Employee findByCuil(String cuil) {
        Optional<Employee> result = employeeRepository.findByCuil(cuil);
        Employee employee = null;

        if (result.isPresent()) {
            employee = result.get();
        } else {
            throw new RuntimeException("Employee not found");
        }

        return employee;
    }

    @Override
    public Employee save(Employee theEmployee) {
        return employeeRepository.save(theEmployee);
    }

    @Override
    public void delete(String id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee save(CreateEmployeeWithAddressDto form) {
        Address homAddress = addressMapper.toAddress(form.getAddress());

        Employee employee = employeeRepository.findByCuil(form.getEmployee().getCuil()).orElseGet(() -> {
            Employee newEmployee = employeeMapper.toEmployee(form.getEmployee());
            return newEmployee;
        });

        employee.setHomeAddress(homAddress);

        return employeeRepository.save(employee);
    }

 

}
