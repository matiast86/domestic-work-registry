package com.springboot.domesticworkregistry.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.domesticworkregistry.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String>{

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByCuil(String cuil);



}
