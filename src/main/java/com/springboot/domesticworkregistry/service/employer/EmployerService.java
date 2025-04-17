package com.springboot.domesticworkregistry.service.employer;

import java.util.List;

import com.springboot.domesticworkregistry.dto.employer.RegisterEmployerDto;
import com.springboot.domesticworkregistry.entities.Employer;
import com.springboot.domesticworkregistry.entities.Job;

public interface EmployerService {

    List<Employer> findAll();

    Employer findById(String id);

    Employer findByEmail(String email);

    Employer save(Employer theEmployer);

    void delete(String theId);

    List<Job> getJobsByEmployee(String employerId, String employeeId);

    Employer save(RegisterEmployerDto registerEmployerDto);

}
