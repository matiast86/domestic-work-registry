package com.springboot.domesticworkregistry.service.job;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.JobRepository;
import com.springboot.domesticworkregistry.dto.job.CreateJobDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Job;
import com.springboot.domesticworkregistry.exceptions.EntityNotFoundException;
import com.springboot.domesticworkregistry.mapper.JobMapper;
import com.springboot.domesticworkregistry.service.contract.ContractService;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ContractService contractService;
    private final JobMapper jobMapper;

    private Double calculateHoursWorked(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start Time should be before end time.");
        }

        long minutes = ChronoUnit.MINUTES.between(startTime, endTime);
        double fractionalHours = minutes / 60.0; // Convert minutes to hours

        return Math.ceil(fractionalHours * 2) / 2;
    }

    private BigDecimal calculatePartialFee(Double workedHours, BigDecimal hourlyRate) {
        return hourlyRate.multiply(BigDecimal.valueOf(workedHours));
    }

    private BigDecimal calculateTotalFee(BigDecimal partialFee, BigDecimal transportationFee) {

        if (partialFee == null || transportationFee == null) {
            throw new IllegalArgumentException("Fees cannot be null");
        }
        if (partialFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Partial fee must be 0 or higher");
        }
        if (transportationFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transportation fee must be 0 or higher");
        }

        return partialFee.add(transportationFee);
    }

    @Autowired
    public JobServiceImpl(JobRepository jobRepository,
            ContractService contractService, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.contractService = contractService;
        this.jobMapper = jobMapper;
    }

    @Override
    public List<Job> findAll() {
        List<Job> jobs = jobRepository.findAll();

        return jobs;
    }

    @Override
    public Job findById(int id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job with id: " + id + " not found"));

        return job;

    }

    @Override
    public Job save(CreateJobDto form, int contractId) {
        Contract contract = contractService.findById(contractId);
        Job job = jobMapper.toJob(form);

        job.setContract(contract);
        LocalTime startTime = job.getStartTime();
        LocalTime endTime = job.getEndTime();
        Double workedHours = this.calculateHoursWorked(startTime, endTime);
        BigDecimal transportationFee = job.getTransportationFee();
        BigDecimal hourlyFee = job.getHourlyRate();
        BigDecimal partialFee = this.calculatePartialFee(workedHours, hourlyFee);
        BigDecimal totalFee = this.calculateTotalFee(partialFee, transportationFee);
        job.setTotalFee(totalFee);
        job.setWorkedHours(workedHours);
        job.setPartialFee(partialFee);

        return jobRepository.save(job);
    }

    @Override
    public void delete(int id) {
        jobRepository.deleteById(id);
    }

    @Override
    public List<Job> getJobsByContract(int contractId) {
        Contract contract = contractService.findById(contractId);
        List<Job> jobs = contract.getJobs();
        return jobs.stream()
                .filter(job -> job.getContract().equals(contract))
                .collect(Collectors.toList());
    }

}
