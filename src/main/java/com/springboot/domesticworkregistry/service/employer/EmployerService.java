package com.springboot.domesticworkregistry.service.employer;

import org.springframework.beans.factory.annotation.Autowired;

import com.springboot.domesticworkregistry.entities.Employer;
import com.springboot.domesticworkregistry.repository.EmployerRepository;

public class EmployerService {

    @Autowired
    EmployerRepository employerRepository;



}
