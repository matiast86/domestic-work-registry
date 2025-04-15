package com.springboot.domesticworkregistry.service.dataCollection;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.entities.Job;
import com.springboot.domesticworkregistry.service.employer.EmployerService;

@Service
public class DataCollectionService {

    private final EmployerService employerService;


    public DataCollectionService(EmployerService employerService) {
       
        this.employerService = employerService;
    }

    // calculate total hours worked by the same employee
    public Double calculateTotalHours(String employerId, String employeeId) {
        List<Job> employeeJobs = employerService.getJobsByEmployee(employerId, employeeId);
        if (employeeJobs.isEmpty()) {
            throw new RuntimeException("No jobs found for the employee.");
        }

        return employeeJobs.stream()
                .mapToDouble(Job::getWorkedHours)
                .sum();

    }

    // Calculate how many hours a certain employee worked in a specific month
    public Double calculateHoursByMonth(String employerId, String employeeId, int year, int month) {
        List<Job> employeeJobs = employerService.getJobsByEmployee(employerId, employeeId);
        if (employeeJobs.isEmpty()) {
            throw new RuntimeException("No jobs found for the employee.");
        }

        return employeeJobs.stream()
                .filter(job -> job.getDate().getYear() == year && job.getDate().getMonthValue() == month)
                .mapToDouble(Job::getWorkedHours)
                .sum();
    }

}
