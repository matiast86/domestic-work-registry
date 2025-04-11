package com.springboot.domesticworkregistry.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String>{

}
