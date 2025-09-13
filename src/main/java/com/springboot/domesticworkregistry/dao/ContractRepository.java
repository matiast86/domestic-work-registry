package com.springboot.domesticworkregistry.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.domesticworkregistry.entities.Contract;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

    @EntityGraph(attributePaths = "employee")
    Optional<Contract> findByIdWtihEmployee(int contractId);

}
