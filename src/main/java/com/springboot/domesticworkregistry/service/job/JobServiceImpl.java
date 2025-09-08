package com.springboot.domesticworkregistry.service.job;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.JobRepository;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Job;
import com.springboot.domesticworkregistry.service.contract.ContractService;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ContractService contractService;

    private Double calculateHoursWorked(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start Time should be before end time.");
        }

        long minutes = ChronoUnit.MINUTES.between(startTime, endTime);
        double fractionalHours = minutes / 60.0; // Convert minutes to hours

        return Math.ceil(fractionalHours * 2) / 2;
    }

    private Double calculatePartialFee(Double workedHours, Double hourlyRate) {
        return workedHours * hourlyRate;
    }

    private Double calculateTotalFee(Double partialFee, Double transportationFee) {

        if (partialFee < 0 || transportationFee < 0) {
            throw new IllegalArgumentException("Partial fee or transportation fee should be 0 or higher");
        }

        return partialFee + transportationFee;
    }

    @Autowired
    public JobServiceImpl(JobRepository jobRepository,
            ContractService contractService) {
        this.jobRepository = jobRepository;
        this.contractService = contractService;
    }

    @Override
    public List<Job> findAll() {
        List<Job> jobs = jobRepository.findAll();

        return jobs;
    }

    @Override
    public Job findById(int id) {
        Optional<Job> result = jobRepository.findById(id);

        Job job = null;

        if (result.isPresent()) {
            job = result.get();
        } else {
            throw new RuntimeException("Job not found.");
        }

        return job;

    }

    @Override
    public Job save(Job job, int contractId) {
        Contract contract = contractService.findById(contractId);
        job.setContract(contract);
        LocalTime startTime = job.getStartTime();
        LocalTime endTime = job.getEndTime();
        Double workedHours = this.calculateHoursWorked(startTime, endTime);
        Double transportationFee = job.getTransportationFee();
        Double hourlyFee = job.getHourlyRate();
        Double partialFee = this.calculatePartialFee(workedHours, hourlyFee);
        Double totalFee = this.calculateTotalFee(partialFee, transportationFee);
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
