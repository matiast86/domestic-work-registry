package com.springboot.domesticworkregistry.service.employer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.EmployerRepository;
import com.springboot.domesticworkregistry.entities.Employer;

@Service
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;

    @Autowired
    public EmployerServiceImpl(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
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

}
