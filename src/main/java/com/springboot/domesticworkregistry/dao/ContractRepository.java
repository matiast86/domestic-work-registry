package com.springboot.domesticworkregistry.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.domesticworkregistry.entities.Contract;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

        @EntityGraph(attributePaths = { "employee", "employee.address" })
        List<Contract> findSummaryByEmployerId(String employerId);

        @EntityGraph(attributePaths = {
                        "employee",
                        "employee.address",
                        "jobs",
                        "jobs.contract",
                        "jobs.contract.schedule",
                        "jobs.contract.schedule.entries"
        })
        Optional<Contract> findDetailById(Integer id);

        Optional<Contract> findByEmployerIdAndEmployeeEmailAndActiveTrue(String employerId, String employeeEmail);

        @EntityGraph(attributePaths = { "schedule", "schedule.entries" })
        @Query("SELECT c FROM Contract c WHERE c.id = :id")
        Optional<Contract> findByIdWithSchedule(@Param("id") Integer id);

}
