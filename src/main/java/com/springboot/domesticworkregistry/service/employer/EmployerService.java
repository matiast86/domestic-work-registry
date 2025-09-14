package com.springboot.domesticworkregistry.service.employer;

import java.util.List;

import com.springboot.domesticworkregistry.dto.employer.RegisterEmployerDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Employee;
import com.springboot.domesticworkregistry.entities.Employer;

public interface EmployerService {

    List<Employer> findAll();

    Employer findById(String id);

    Employer findByEmail(String email);

    Employer save(Employer theEmployer);

    void delete(String theId);

    Employer save(RegisterEmployerDto registerEmployerDto);

    List<Contract> findContractsByEmployer(String id);

}
