package com.springboot.domesticworkregistry.service.dataCollection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
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

    // --- Core Helpers ---
    private List<Job> getEmployeeJobs(int contractId) {
        List<Job> jobs = jobService.getJobsByContract(contractId);
        if (jobs.isEmpty()) {
            throw new NoJobsFoundException("No jobs found for the employee.");
        }
        return jobs;
    }

    private List<Job> filterJobs(List<Job> jobs, Predicate<Job> predicate) {
        return jobs.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private BigDecimal sumJobs(List<Job> jobs, Function<Job, BigDecimal> mapper) {
        return jobs.stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private double sumHours(List<Job> jobs) {
        return jobs.stream()
                .mapToDouble(job -> job.getWorkedHours() != null ? job.getWorkedHours() : 0)
                .sum();
    }

    private BigDecimal averageJobs(List<Job> jobs, Function<Job, BigDecimal> mapper) {
        if (jobs.isEmpty())
            return BigDecimal.ZERO;
        BigDecimal total = sumJobs(jobs, mapper);
        return total.divide(BigDecimal.valueOf(jobs.size()), 2, RoundingMode.HALF_UP);
    }

    // --- Generic Filters ---
    private Predicate<Job> byMonth(int year, int month) {
        return job -> job.getDate().getYear() == year && job.getDate().getMonthValue() == month;
    }

    private Predicate<Job> byYear(int year) {
        return job -> job.getDate().getYear() == year;
    }

    // --- Hours ---
    public Double calculateTotalHours(int contractId) {
        return sumHours(getEmployeeJobs(contractId));
    }

    public Double calculateHoursByMonth(int contractId, int year, int month) {
        return sumHours(filterJobs(getEmployeeJobs(contractId), byMonth(year, month)));
    }

    // --- Fees (Generic) ---
    public BigDecimal calculateSum(int contractId, Function<Job, BigDecimal> mapper) {
        return sumJobs(getEmployeeJobs(contractId), mapper);
    }

    public BigDecimal calculateSumByMonth(int contractId, int year, int month, Function<Job, BigDecimal> mapper) {
        return sumJobs(filterJobs(getEmployeeJobs(contractId), byMonth(year, month)), mapper);
    }

    public BigDecimal calculateSumByYear(int contractId, int year, Function<Job, BigDecimal> mapper) {
        List<Job> jobs = filterJobs(getEmployeeJobs(contractId), byYear(year));
        if (jobs.isEmpty())
            throw new NoJobsFoundException("No jobs found in the specified year.");
        return sumJobs(jobs, mapper);
    }

    public BigDecimal calculateAverage(int contractId, Function<Job, BigDecimal> mapper) {
        return averageJobs(getEmployeeJobs(contractId), mapper);
    }

    public BigDecimal calculateAverageByYear(int contractId, int year, Function<Job, BigDecimal> mapper) {
        List<Job> jobs = filterJobs(getEmployeeJobs(contractId), byYear(year));
        if (jobs.isEmpty())
            throw new NoJobsFoundException("No jobs found in the specified year.");
        return averageJobs(jobs, mapper);
    }

}
