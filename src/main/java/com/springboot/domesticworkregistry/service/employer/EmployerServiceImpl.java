package com.springboot.domesticworkregistry.service.employer;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.EmployerRepository;
import com.springboot.domesticworkregistry.dto.employer.RegisterEmployerDto;
import com.springboot.domesticworkregistry.entities.Address;
import com.springboot.domesticworkregistry.entities.Employee;
import com.springboot.domesticworkregistry.entities.Employer;
import com.springboot.domesticworkregistry.entities.Job;
import com.springboot.domesticworkregistry.enums.Role;
import com.springboot.domesticworkregistry.service.employee.EmployeeService;

@Service
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployerServiceImpl(EmployerRepository employerRepository, EmployeeService employeeService,
            PasswordEncoder passwordEncoder) {
        this.employerRepository = employerRepository;
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
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
    public List<Job> getJobsByEmployee(String employerId, String employeeId) {
        Employee employee = employeeService.findById(employeeId);
        Employer employer = this.findById(employerId);
        List<Job> jobs = employer.getJobs();
        return jobs.stream()
                .filter(job -> job.getEmployee().equals(employee))
                .collect(Collectors.toList());

    }

    @Override
    public Employer save(RegisterEmployerDto registerEmployerDto) {

        //check if employer exists 
        if(employerRepository.findByEmail(registerEmployerDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered.");
        }

        Employer employer = new Employer();
        Address address = new Address();
        employer.setFirstName(registerEmployerDto.getFirstName());
        employer.setLastName(registerEmployerDto.getLastName());
        employer.setAge(registerEmployerDto.getAge());
        employer.setEmail(registerEmployerDto.getEmail().toLowerCase());
        employer.setIdentificationNumber(registerEmployerDto.getIdentificationNumber());
        employer.setPhone(registerEmployerDto.getPhone());
        String hashedPassword = passwordEncoder.encode(registerEmployerDto.getPassword());
        employer.setPassword(hashedPassword);
        address.setStreet(registerEmployerDto.getStreet());
        address.setNumber(registerEmployerDto.getNumber());
        address.setCity(registerEmployerDto.getCity());
        address.setPostalCode(registerEmployerDto.getPostalCode());
        address.setCountry(registerEmployerDto.getCountry());
        employer.setAddress(address);
        employer.setRole(Role.EMPLOYER);
        employer.setCreatedAt(new Date());
        employer.setActive(true);

        return employerRepository.save(employer);

    }

}
