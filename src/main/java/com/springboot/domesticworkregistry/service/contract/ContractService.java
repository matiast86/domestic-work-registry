package com.springboot.domesticworkregistry.service.contract;

import java.util.List;

import com.springboot.domesticworkregistry.dto.contract.CreateContractWithEmployeeDto;
import com.springboot.domesticworkregistry.entities.Contract;

public interface ContractService {
    public List<Contract> findAll();

    public Contract save(String employerEmail, CreateContractWithEmployeeDto form);

    public Contract findById(String id);

}
