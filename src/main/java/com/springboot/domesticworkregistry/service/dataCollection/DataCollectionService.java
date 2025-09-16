package com.springboot.domesticworkregistry.service.dataCollection;

import java.math.BigDecimal;
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
    private List<Job> getEmployeeJobs(int contractId) {
        List<Job> jobs = jobService.getJobsByContract(contractId);
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
    public Double calculateTotalHours(int contractId) {
        return getEmployeeJobs(contractId).stream()
                .mapToDouble(Job::getWorkedHours)
                .sum();

    }

    // Calculate how many hours a certain employee worked in a specific month
    public Double calculateHoursByMonth(int contractId, int year, int month) {
        List<Job> monthlyJobs = filterJobsByMonth(getEmployeeJobs(contractId), year, month);

        return monthlyJobs
                .stream()
                .mapToDouble(Job::getWorkedHours)
                .sum();
    }

    // Calculates the total fee paid to a specific employee
    public BigDecimal calculateTotalFee(int contractId) {
        return getEmployeeJobs(contractId).stream()
                .map(Job::getTotalFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calculate total fee in a specific month
    public BigDecimal calculateTotalFeeByMonth(int contractId, int year, int month) {
        List<Job> monthlyJobs = filterJobsByMonth(getEmployeeJobs(contractId), year, month);

        return monthlyJobs.stream()
                .map(Job::getTotalFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calculate fee before adding transportation
    public BigDecimal calculatePartialFee(int contractId) {
        return getEmployeeJobs(contractId).stream()
                .map(Job::getPartialFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calculate fee before adding transportation in a specific month
    public BigDecimal calculatePartialFeeByMonth(int contractId, int year, int month) {
        List<Job> monthlyJobs = filterJobsByMonth(getEmployeeJobs(contractId), year, month);

        return monthlyJobs
                .stream()
                .map(Job::getPartialFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calculate the sum of hourly fees needed to calculate the average
    public BigDecimal calculateTotalHourlyFee(int contractId) {
        return getEmployeeJobs(contractId).stream()
                .map(Job::getHourlyRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calculate the average of the hourly fees
    public BigDecimal calculateAverageHourlyFee(int contractId) {
        BigDecimal hourlyFee = calculateTotalHourlyFee(contractId);
        return hourlyFee.divide(BigDecimal.valueOf(getEmployeeJobs(contractId).size()));
    }

    // Calculate the average of hourly fee in a year
    public BigDecimal calculateAverageHourlyFeeByYear(int contractId, int year) {
        List<Job> jobsInYear = getEmployeeJobs(contractId).stream()
                .filter(job -> job.getDate().getYear() == year)
                .collect(Collectors.toList());

        if (jobsInYear.isEmpty()) {
            throw new NoJobsFoundException("No jobs found for the employee in the specified year.");
        }

        BigDecimal totalHourlyFee = jobsInYear
                .stream()
                .map(Job::getHourlyRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalHourlyFee.divide(BigDecimal.valueOf(jobsInYear.size()));

    }

    // Calculate transportation fee
    public BigDecimal calculateTransportationFee(int contractId) {
        return getEmployeeJobs(contractId)
                .stream()
                .map(Job::getTransportationFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calculate transportation fee in a month
    public BigDecimal calculateTransportationFeeByMonth(int contractId, int year, int month) {
        List<Job> monthlyJobs = filterJobsByMonth(getEmployeeJobs(contractId), year, month);

        return monthlyJobs
                .stream()
                .map(Job::getTransportationFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
