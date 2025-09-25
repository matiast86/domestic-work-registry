package com.springboot.domesticworkregistry.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.Contract;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

    @EntityGraph(attributePaths = { "employee", "employee.address", "jobs" })
    Optional<Contract> findById(Integer id);

    @EntityGraph(attributePaths = { "employee", "employee.address", "jobs" })
    List<Contract> findAllByEmployerId(String employerId);

    Optional<Contract> findByEmployerIdAndEmployeeEmailAndActiveTrue(String employerId, String employeeEmail);

}
