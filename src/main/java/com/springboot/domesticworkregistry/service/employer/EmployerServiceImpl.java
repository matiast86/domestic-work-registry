package com.springboot.domesticworkregistry.service.employer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.EmployerRepository;
import com.springboot.domesticworkregistry.entities.Employee;
import com.springboot.domesticworkregistry.entities.Employer;
import com.springboot.domesticworkregistry.entities.Job;
import com.springboot.domesticworkregistry.service.employee.EmployeeService;

@Service
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    private final EmployeeService employeeService;

    @Autowired
    public EmployerServiceImpl(EmployerRepository employerRepository, EmployeeService employeeService) {
        this.employerRepository = employerRepository;
        this.employeeService = employeeService;
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
                    .filter(job ->  job.getEmployee().equals(employee))
                    .collect(Collectors.toList());
        
    }

}
