package com.springboot.domesticworkregistry.service.dataCollection;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.domesticworkregistry.entities.Job;
import com.springboot.domesticworkregistry.exceptions.NoJobsFoundException;
import com.springboot.domesticworkregistry.service.job.JobService;

@Service
@Transactional(readOnly = true)
public class DataCollectionService {

    private final JobService jobService;

    public DataCollectionService(JobService jobService) {

        this.jobService = jobService;
    }

    // Helper getEmployeeJobs
    private List<Job> getEmployeeJobs(String employerId, String employeeId) {
        List<Job> jobs = jobService.getJobsByEmployee(employerId, employeeId);
        if (jobs.isEmpty()) {
            throw new NoJobsFoundException("No jobs found for the employee.");
        }
        return jobs;
    }

    // Helper to filter jobs by year and month
    private List<Job> filterJobsByMonth(List<Job> jobs, int year, int month) {
        return jobs.stream()
                .filter(job -> job.getDate().getYear() == year && job.getDate().getMonthValue() == month)
                .collect(Collectors.toList());
    }

    // calculate total hours worked by the same employee
    public Double calculateTotalHours(String employerId, String employeeId) {
        return getEmployeeJobs(employerId, employeeId).stream()
                .mapToDouble(Job::getWorkedHours)
                .sum();

    }

    // Calculate how many hours a certain employee worked in a specific month
    public Double calculateHoursByMonth(String employerId, String employeeId, int year, int month) {
        List<Job> monthlyJobs = filterJobsByMonth(getEmployeeJobs(employerId, employeeId), year, month);

        return monthlyJobs
                .stream()
                .mapToDouble(Job::getWorkedHours)
                .sum();
    }

    // Calculates the total fee paid to a specific employee
    public Double calculateTotalFee(String employerId, String employeeId) {
        return getEmployeeJobs(employerId, employeeId).stream()
                .mapToDouble(Job::getTotalFee)
                .sum();
    }

    // Calculate total fee in a specific month
    public Double calculateTotalFeeByMonth(String employerId, String employeeId, int year, int month) {
        List<Job> monthlyJobs = filterJobsByMonth(getEmployeeJobs(employerId, employeeId), year, month);

        return monthlyJobs.stream()
                .mapToDouble(Job::getTotalFee)
                .sum();
    }

    // Calculate fee before adding transportation
    public Double calculatePartialFee(String employerId, String employeeId) {
        return getEmployeeJobs(employerId, employeeId).stream()
                .mapToDouble(Job::getPartialFee)
                .sum();
    }

    // Calculate fee before adding transportation in a specific month
    public Double calculatePartialFeeByMonth(String employerId, String employeeId, int year, int month) {
        List<Job> monthlyJobs = filterJobsByMonth(getEmployeeJobs(employerId, employeeId), year, month);

        return monthlyJobs
                .stream()
                .mapToDouble(Job::getPartialFee)
                .sum();
    }

    // Calculate the sum of hourly fees needed to calculate the average
    public Double calculateTotalHourlyFee(String employerId, String employeeId) {
        return getEmployeeJobs(employerId, employeeId).stream()
                .mapToDouble(Job::getHourlyRate)
                .sum();
    }

    // Calculate the average of the hourly fees
    public Double calculateAverageHourlyFee(String employerId, String employeeId) {
        Double hourlyFee = calculateTotalHourlyFee(employerId, employeeId);
        return hourlyFee / getEmployeeJobs(employerId, employeeId).size();
    }

    // Calculate the average of hourly fee in a year
    public Double calculateAverageHourlyFeeByYear(String employerId, String employeeId, int year) {
        List<Job> jobsInYear = getEmployeeJobs(employerId, employeeId).stream()
                .filter(job -> job.getDate().getYear() == year)
                .collect(Collectors.toList());

        if (jobsInYear.isEmpty()) {
            throw new NoJobsFoundException("No jobs found for the employee in the specified year.");
        }

        double totalHourlyFee = jobsInYear
                .stream()
                .mapToDouble(Job::getHourlyRate)
                .sum();

        return totalHourlyFee / jobsInYear.size();

    }

    // Calculate transportation fee
    public Double calculateTransportationFee(String employerId, String employeeId) {
        return getEmployeeJobs(employerId, employeeId)
                .stream()
                .mapToDouble(Job::getTransportationFee)
                .sum();
    }

    // Calculate transportation fee in a month
    public Double calculateTransportationFeeByMonth(String employerId, String employeeId, int year, int month) {
        List<Job> monthlyJobs = filterJobsByMonth(getEmployeeJobs(employerId, employeeId), year, month);

        return monthlyJobs
                .stream()
                .mapToDouble(Job::getTransportationFee)
                .sum();
    }

}
