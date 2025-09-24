package com.springboot.domesticworkregistry.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.springboot.domesticworkregistry.dao.EmployeeRepository;
import com.springboot.domesticworkregistry.dao.EmployerRepository;

public class CustomUserDetailsService implements UserDetailsService {

    private EmployerRepository employerRepository;
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return employerRepository.findByEmail(email).map(UserDetails.class::cast)
                .or(() -> employeeRepository.findByEmail(email).map(UserDetails.class::cast))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

}
