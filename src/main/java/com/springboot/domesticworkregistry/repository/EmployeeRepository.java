package com.springboot.domesticworkregistry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String>{

}
