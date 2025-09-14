package com.springboot.domesticworkregistry.service.employer;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.EmployerRepository;
import com.springboot.domesticworkregistry.entities.Employer;

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

        return employer;
    }

}
