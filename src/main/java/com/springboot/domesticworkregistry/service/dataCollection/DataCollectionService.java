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

@Service
@Transactional(readOnly = true)
public class DataCollectionService {

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

    private BigDecimal sumHours(List<Job> jobs) {
        return jobs.stream()
                .map(Job::getWorkedHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
    public BigDecimal calculateTotalHours(List<Job> jobs) {
        return sumHours(jobs);
    }

    public BigDecimal calculateHoursByMonth(List<Job> jobs, int year, int month) {
        return sumHours(filterJobs(jobs, byMonth(year, month)));
    }

    public BigDecimal calculateHoursByYear(List<Job> jobs, int year) {
        List<Job> filteredJobs = filterJobs(jobs, byYear(year));
        if (filteredJobs.isEmpty())
            throw new NoJobsFoundException("No jobs found in the specified year.");
        return sumHours(filteredJobs);
    }

    // --- Fees (Generic) ---
    public BigDecimal calculateSum(List<Job> jobs, Function<Job, BigDecimal> mapper) {
        return sumJobs(jobs, mapper);
    }

    public BigDecimal calculateSumByMonth(List<Job> jobs, int year, int month, Function<Job, BigDecimal> mapper) {
        return sumJobs(filterJobs(jobs, byMonth(year, month)), mapper);
    }

    public BigDecimal calculateSumByYear(List<Job> jobs, int year, Function<Job, BigDecimal> mapper) {
        List<Job> filteredJobs = filterJobs(jobs, byYear(year));
        if (filteredJobs.isEmpty())
            throw new NoJobsFoundException("No jobs found in the specified year.");
        return sumJobs(filteredJobs, mapper);
    }

    public BigDecimal calculateAverage(List<Job> jobs, Function<Job, BigDecimal> mapper) {
        return averageJobs(jobs, mapper);
    }

    public BigDecimal calculateAverageByMonth(List<Job> jobs, int year, int month, Function<Job, BigDecimal> mapper) {
        return averageJobs(filterJobs(jobs, byMonth(year, month)), mapper);
    }

    public BigDecimal calculateAverageByYear(List<Job> jobs, int year, Function<Job, BigDecimal> mapper) {
        List<Job> filteredJobs = filterJobs(jobs, byYear(year));
        if (filteredJobs.isEmpty())
            throw new NoJobsFoundException("No jobs found in the specified year.");
        return averageJobs(filteredJobs, mapper);
    }

}
