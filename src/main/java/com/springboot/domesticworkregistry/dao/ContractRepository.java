package com.springboot.domesticworkregistry.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.Contract;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

}
