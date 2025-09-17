package com.springboot.domesticworkregistry.service.job;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.springboot.domesticworkregistry.dao.JobRepository;
import com.springboot.domesticworkregistry.dto.job.CreateJobDto;
import com.springboot.domesticworkregistry.dto.job.JobsReportDto;
import com.springboot.domesticworkregistry.dto.job.JobsTableDto;
import com.springboot.domesticworkregistry.dto.job.JobsTotalsDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Job;
import com.springboot.domesticworkregistry.exceptions.EntityNotFoundException;
import com.springboot.domesticworkregistry.mapper.JobMapper;
import com.springboot.domesticworkregistry.service.contract.ContractService;
import com.springboot.domesticworkregistry.service.dataCollection.DataCollectionService;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ContractService contractService;
    private final JobMapper jobMapper;
    private final DataCollectionService dataCollectionService;

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

    private String getMonthNameInSpanish(LocalDate date) {
        Locale spanishArgentina = new Locale("es", "AR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", spanishArgentina);
        return date.format(formatter);

    }

    @Autowired
    public JobServiceImpl(JobRepository jobRepository,
            ContractService contractService, JobMapper jobMapper, DataCollectionService dataCollectionService) {
        this.jobRepository = jobRepository;
        this.contractService = contractService;
        this.jobMapper = jobMapper;
        this.dataCollectionService = dataCollectionService;
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

    @Override
    public JobsReportDto getJobsByContracDto(int contractId) {
        Contract contract = contractService.findById(contractId);
        List<Job> jobs = contract.getJobs();
        List<JobsTableDto> dtoList = new ArrayList<>();
        JobsTotalsDto totals = new JobsTotalsDto();
        JobsReportDto reportDto = new JobsReportDto(dtoList, totals);

        for (Job job : jobs) {
            int yearValue = job.getDate().getYear();
            int monthValue = job.getDate().getMonthValue();
            BigDecimal hourlyFee = this.dataCollectionService.calculateAverageByMonth(jobs, yearValue, monthValue,
                    Job::getHourlyRate);
            Double workedHours = this.dataCollectionService.calculateHoursByMonth(jobs, yearValue, monthValue);
            BigDecimal subtotal = this.dataCollectionService.calculateSumByMonth(jobs, yearValue, monthValue,
                    Job::getPartialFee);
            BigDecimal transportationFee = this.dataCollectionService.calculateSumByMonth(jobs, yearValue, monthValue,
                    Job::getTransportationFee);
            BigDecimal total = this.dataCollectionService.calculateSumByMonth(jobs, yearValue, monthValue,
                    Job::getTotalFee);
            totals.setHourlyFeeTotal(this.dataCollectionService.calculateAverageByYear(jobs, yearValue,
                    Job::getHourlyRate));
            totals.setWorkedHoursTotal(this.dataCollectionService.calculateHoursByYear(jobs, yearValue));
            totals.setSubtotalTotal(this.dataCollectionService.calculateSumByYear(jobs, yearValue,
                    Job::getPartialFee));
            totals.setTransportationFeeTotal(this.dataCollectionService.calculateSumByYear(jobs, yearValue,
                    Job::getTransportationFee));
            totals.setTotalTotal(this.dataCollectionService.calculateSumByYear(jobs, yearValue, Job::getTotalFee));
            String jobMonth = StringUtils.capitalize(this.getMonthNameInSpanish(job.getDate()));

            JobsTableDto dto = new JobsTableDto(contractId, yearValue, jobMonth, workedHours, hourlyFee, subtotal,
                    transportationFee, total);
            dtoList.add(dto);
        }

        return reportDto;

    }
}
