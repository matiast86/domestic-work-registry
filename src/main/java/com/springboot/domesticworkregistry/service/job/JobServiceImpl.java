package com.springboot.domesticworkregistry.service.job;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.springboot.domesticworkregistry.dao.JobRepository;
import com.springboot.domesticworkregistry.dto.job.CreateJobDto;
import com.springboot.domesticworkregistry.dto.job.JobsMonthlyReportDto;
import com.springboot.domesticworkregistry.dto.job.JobsMonthlyTableDto;
import com.springboot.domesticworkregistry.dto.job.JobsReportDto;
import com.springboot.domesticworkregistry.dto.job.JobsTableDto;
import com.springboot.domesticworkregistry.dto.job.JobsTotalsDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Job;
import com.springboot.domesticworkregistry.exceptions.EntityNotFoundException;
import com.springboot.domesticworkregistry.mapper.JobMapper;
import com.springboot.domesticworkregistry.mapper.JobsMonthlyTableMapper;
import com.springboot.domesticworkregistry.service.contract.ContractService;
import com.springboot.domesticworkregistry.service.dataCollection.DataCollectionService;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ContractService contractService;
    private final JobMapper jobMapper;
    private final JobsMonthlyTableMapper tableMapper;
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
            ContractService contractService, JobMapper jobMapper, JobsMonthlyTableMapper tableMapper,
            DataCollectionService dataCollectionService) {
        this.jobRepository = jobRepository;
        this.contractService = contractService;
        this.jobMapper = jobMapper;
        this.tableMapper = tableMapper;
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
        Double workedHours = calculateHoursWorked(startTime, endTime);
        BigDecimal transportationFee = job.getTransportationFee();
        BigDecimal hourlyFee = job.getHourlyRate();
        BigDecimal partialFee = calculatePartialFee(workedHours, hourlyFee);
        BigDecimal totalFee = calculateTotalFee(partialFee, transportationFee);
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
        List<JobsTotalsDto> totalsList = new ArrayList<>();

        // Group by year and then month
        Map<Integer, Map<Integer, List<Job>>> jobsByYearMonth = jobs.stream().collect(Collectors.groupingBy(
                job -> job.getDate().getYear(),
                Collectors.groupingBy(job -> job.getDate().getMonthValue())));

        for (var yearEntry : jobsByYearMonth.entrySet()) {
            int year = yearEntry.getKey();
            Map<Integer, List<Job>> jobsByMonth = yearEntry.getValue();

            // Monthly rows
            for (var monthEntry : jobsByMonth.entrySet()) {
                int month = monthEntry.getKey();
                List<Job> monthlyJobs = monthEntry.getValue();
                BigDecimal hourlyFee = dataCollectionService.calculateAverage(monthlyJobs, Job::getHourlyRate);
                Double workedHours = dataCollectionService.calculateTotalHours(monthlyJobs);
                BigDecimal subtotal = dataCollectionService.calculateSum(monthlyJobs,
                        Job::getPartialFee);
                BigDecimal transportationFee = dataCollectionService.calculateSum(monthlyJobs,
                        Job::getTransportationFee);
                BigDecimal total = dataCollectionService.calculateSum(monthlyJobs,
                        Job::getTotalFee);
                String jobMonth = StringUtils.capitalize(getMonthNameInSpanish(LocalDate.of(year, month, 1)));
                dtoList.add(
                        new JobsTableDto(contractId, LocalDate.of(year, month, 1), year, month, jobMonth, workedHours,
                                hourlyFee, subtotal, transportationFee, total));
            }
            // yearly totals row
            List<Job> yearlyJobs = yearEntry.getValue().values().stream().flatMap(List::stream).toList();

            JobsTotalsDto yearTotals = new JobsTotalsDto(year, dataCollectionService.calculateTotalHours(yearlyJobs),
                    dataCollectionService.calculateAverage(yearlyJobs, Job::getHourlyRate),
                    dataCollectionService.calculateSum(yearlyJobs, Job::getPartialFee),
                    dataCollectionService.calculateSum(yearlyJobs, Job::getTransportationFee),
                    dataCollectionService.calculateSum(yearlyJobs, Job::getTotalFee));

            totalsList.add(yearTotals);
        }

        // sort by date
        dtoList.sort(Comparator.comparing(JobsTableDto::getDate));

        return new JobsReportDto(contractId, dtoList, totalsList);

    }

    @Override
    public JobsMonthlyReportDto getMonthlyJobsByContract(int contractId, int year, int month) {
        Contract contract = contractService.findById(contractId);
        List<Job> jobs = contract.getJobs().stream()
                .filter(job -> job.getDate().getYear() == year && job.getDate().getMonthValue() == month).toList();
        List<JobsMonthlyTableDto> tables = new ArrayList<>();
        String jobMonth = StringUtils.capitalize(getMonthNameInSpanish(LocalDate.of(year, month, 1)));

        for (Job job : jobs) {
            JobsMonthlyTableDto tableDto = tableMapper.toDo(job);
            tableDto.setYear(year);
            tableDto.setMonth(jobMonth);
            tables.add(tableDto);

        }

        BigDecimal hourlyFeeTotal = dataCollectionService.calculateAverage(jobs, Job::getHourlyRate);
        Double workedHoursTotal = dataCollectionService.calculateTotalHours(jobs);
        BigDecimal subtotalTotal = dataCollectionService.calculateSum(jobs,
                Job::getPartialFee);
        BigDecimal transportationFeeTotal = dataCollectionService.calculateSum(jobs,
                Job::getTransportationFee);
        BigDecimal totalTotal = dataCollectionService.calculateSum(jobs,
                Job::getTotalFee);

        JobsTotalsDto totalsDto = new JobsTotalsDto(year, workedHoursTotal, hourlyFeeTotal, subtotalTotal,
                transportationFeeTotal, totalTotal);

        return new JobsMonthlyReportDto(tables, totalsDto);

    }

    @Override
    public CreateJobDto getJobDto(int jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job with id " + jobId + " not found"));

        return new CreateJobDto(job.getDate(), job.getStartTime(), job.getEndTime(), job.getHourlyRate(),
                job.getTransportationFee());

    }

    @Override
    public Job update(CreateJobDto form, int jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job with id " + jobId + " not found"));

        LocalTime startTime = form.getStartTime();
        LocalTime endTime = form.getEndTime();
        Double workedHours = calculateHoursWorked(startTime, endTime);
        BigDecimal transportationFee = form.getTransportationFee();
        BigDecimal hourlyFee = form.getHourlyRate();
        BigDecimal partialFee = calculatePartialFee(workedHours, hourlyFee);
        BigDecimal totalFee = calculateTotalFee(partialFee, transportationFee);
        job.setDate(form.getDate());
        job.setStartTime(startTime);
        job.setEndTime(endTime);
        job.setHourlyRate(hourlyFee);
        job.setTransportationFee(transportationFee);
        job.setTotalFee(totalFee);
        job.setWorkedHours(workedHours);
        job.setPartialFee(partialFee);

        return jobRepository.save(job);
    }

}
