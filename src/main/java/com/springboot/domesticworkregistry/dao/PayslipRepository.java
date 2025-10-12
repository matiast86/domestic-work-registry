package com.springboot.domesticworkregistry.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.Payslip;

public interface PayslipRepository extends JpaRepository<Payslip, Integer> {

}
