package com.springboot.domesticworkregistry.service.contract;

import java.util.List;

import com.springboot.domesticworkregistry.dto.contract.CreateEmployeeFormDto;
import com.springboot.domesticworkregistry.entities.Contract;

public interface ContractService {
    public List<Contract> findAll();

    public List<Contract> findAllByEmployer(String employerId);

    public Contract save(String employerEmail, CreateEmployeeFormDto form);

    public Contract findById(int id);

}
