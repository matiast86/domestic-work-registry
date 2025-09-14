package com.springboot.domesticworkregistry.service.employer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.EmployerRepository;
import com.springboot.domesticworkregistry.dto.employer.RegisterEmployerDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Employer;
import com.springboot.domesticworkregistry.mapper.EmployerMapper;

@Service
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployerMapper employerMapper;

    @Autowired
    public EmployerServiceImpl(EmployerRepository employerRepository,
            PasswordEncoder passwordEncoder, EmployerMapper employerMapper) {
        this.employerRepository = employerRepository;
        this.passwordEncoder = passwordEncoder;
        this.employerMapper = employerMapper;
    }

    @Override
    public List<Employer> findAll() {
        return employerRepository.findAll();
    }

    @Override
    public Employer findById(String id) {
        Optional<Employer> result = employerRepository.findById(id);

        Employer theEmployer = null;

        if (result.isPresent()) {
            theEmployer = result.get();
        } else {
            throw new RuntimeException("Employer not found");
        }

        return theEmployer;

    }

    @Override
    public Employer findByEmail(String email) {
        Optional<Employer> result = employerRepository.findByEmail(email);

        Employer theEmployer = null;

        if (result.isPresent()) {
            theEmployer = result.get();
        } else {
            throw new RuntimeException("Employer not found");
        }

        return theEmployer;
    }

    @Override
    public Employer save(Employer theEmployer) {
        return employerRepository.save(theEmployer);
    }

    @Override
    public void delete(String theId) {
        employerRepository.deleteById(theId);
    }

    @Override
    public Employer save(RegisterEmployerDto dto) {

        // check if employer exists
        if (employerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered.");
        }

        Employer employer = employerMapper.toEmployer(dto);
        employer.setPassword(passwordEncoder.encode(dto.getPassword()));
        employer.setEmail(dto.getEmail().toLowerCase());

        return employerRepository.save(employer);

    }

    @Override
    public List<Contract> findContractsByEmployer(String id) {
        Optional<Employer> result = employerRepository.findById(id);

        Employer theEmployer = null;

        if (result.isPresent()) {
            theEmployer = result.get();
        } else {
            throw new RuntimeException("Employer not found");
        }

        List<Contract> contracts = theEmployer.getContracts();

        return contracts;
    }

}
