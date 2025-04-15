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

    // Helper getEmployeeJobs
    private List<Job> getEmployeeJobs(String employerId, String employeeId) {
        List<Job> jobs = employerService.getJobsByEmployee(employerId, employeeId);
        if (jobs.isEmpty()) {
            throw new RuntimeException("No jobs found for the employee.");
        }
        return jobs;
    }

    // calculate total hours worked by the same employee
    public Double calculateTotalHours(String employerId, String employeeId) {
        return getEmployeeJobs(employerId, employeeId).stream()
                .mapToDouble(Job::getWorkedHours)
                .sum();

    }

    // Calculate how many hours a certain employee worked in a specific month
    public Double calculateHoursByMonth(String employerId, String employeeId, int year, int month) {

        return getEmployeeJobs(employerId, employeeId).stream()
                .filter(job -> job.getDate().getYear() == year && job.getDate().getMonthValue() == month)
                .mapToDouble(Job::getWorkedHours)
                .sum();
    }

}
