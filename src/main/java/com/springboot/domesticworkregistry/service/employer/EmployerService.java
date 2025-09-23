package com.springboot.domesticworkregistry.service.employer;

import java.util.List;

import com.springboot.domesticworkregistry.dto.employer.RegisterEmployerDto;
import com.springboot.domesticworkregistry.dto.employer.UpdateEmployerDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Employer;

public interface EmployerService {

    List<Employer> findAll();

    Employer findById(String id);

    Employer findByEmail(String email);

    UpdateEmployerDto getEmployerDto(String id);

    Employer update(UpdateEmployerDto form, String id);

    void delete(String theId);

    Employer save(RegisterEmployerDto registerEmployerDto);

    List<Contract> findContractsByEmployer(String id);

}
