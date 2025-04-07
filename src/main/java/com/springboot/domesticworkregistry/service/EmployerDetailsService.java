package com.springboot.domesticworkregistry.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.entities.Employer;
import com.springboot.domesticworkregistry.repository.EmployerRepository;

@Service
public class EmployerDetailsService implements UserDetailsService{

    EmployerRepository employerRepository;


      @Autowired
    public EmployerDetailsService(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employer employer = employerRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
            employer.getEmail(), employer.getPassword(), employer.getAuthorities());
    }

}
