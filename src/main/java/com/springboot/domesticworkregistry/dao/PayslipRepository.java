package com.springboot.domesticworkregistry.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.domesticworkregistry.entities.Payslip;

public interface PayslipRepository extends JpaRepository<Payslip, Integer> {

    @Query("SELECT p from Payslip p WHERE p.contract.id = :contractId ORDER BY p.year DESC, p.month DESC")
    List<Payslip> findByContractId(@Param("contractId") Integer contractId);

    @EntityGraph(attributePaths = { "contract", "contract.employer", "contract.employee" })
    @Query("SELECT p from Payslip p WHERE p.id = :id")
    Optional<Payslip> findByIdWithContract(@Param("id") Integer id);
}
