package com.springboot.domesticworkregistry.service.payslip;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.PayslipRepository;
import com.springboot.domesticworkregistry.dto.payslip.CreatePayslipDto;
import com.springboot.domesticworkregistry.dto.payslip.PayslipDetailsDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Job;
import com.springboot.domesticworkregistry.entities.Payslip;
import com.springboot.domesticworkregistry.entities.ScheduleEntry;
import com.springboot.domesticworkregistry.enums.EmploymentType;
import com.springboot.domesticworkregistry.mapper.PayslipDetailsMapper;
import com.springboot.domesticworkregistry.service.contract.ContractService;
import com.springboot.domesticworkregistry.service.dataCollection.DataCollectionService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PayslipServiceImpl implements PayslipService {

    private final PayslipRepository payslipRepository;

    private final ContractService contractService;

    private final DataCollectionService dataCollectionService;

    private final PayslipDetailsMapper detailsMapper;

    private static final int SCALE = 2;

    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    public PayslipServiceImpl(PayslipRepository payslipRepository, ContractService contractService,
            DataCollectionService dataCollectionService, PayslipDetailsMapper detailsMapper) {
        this.payslipRepository = payslipRepository;
        this.contractService = contractService;
        this.dataCollectionService = dataCollectionService;
        this.detailsMapper = detailsMapper;

    }

    private List<Job> getMonthlyJobs(Contract contract, int year, int month) {
        return contract.getJobs().stream()
                .filter(job -> job.getDate().getYear() == year && job.getDate().getMonthValue() == month).toList();
    }

    private String getMonthNameInSpanish(LocalDate date) {
        Locale spanishArgentina = Locale.of("es", "AR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM", spanishArgentina);
        return date.format(formatter);

    }

    private BigDecimal getWorkedHours(List<Job> jobs, int year, int month) {

        return dataCollectionService.calculateHoursByMonth(jobs, year, month);
    }

    private BigDecimal getPartial(List<Job> jobs, int year, int month) {

        return dataCollectionService.calculateSumByMonth(jobs, year, month, Job::getTotalFee);
    }

    private BigDecimal getTransportation(Contract contract, int year, int month) {
        List<Job> monthlyJobs = getMonthlyJobs(contract, year, month);
        if (contract.getEmploymentType() == EmploymentType.HOURLY) {

            return dataCollectionService.calculateSumByMonth(monthlyJobs, year, month, Job::getTransportationFee);
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal getTotalMonthlyHours(Contract contract) {
        List<ScheduleEntry> entries = contract.getSchedule().getEntries();
        BigDecimal weeklyHours = BigDecimal.ZERO;
        for (ScheduleEntry entry : entries) {
            long minutes = ChronoUnit.MINUTES.between(entry.getStartTime(), entry.getEndTime());
            BigDecimal fractionalHours = BigDecimal.valueOf(minutes)
                    .divide(BigDecimal.valueOf(60), SCALE, ROUNDING);
            BigDecimal multiplier = BigDecimal.valueOf(2);
            BigDecimal rounded = fractionalHours.multiply(multiplier)
                    .setScale(0, RoundingMode.HALF_UP)
                    .divide(multiplier, 1, ROUNDING);
            weeklyHours = weeklyHours.add(rounded);
        }

        return weeklyHours.divide(BigDecimal.valueOf(7), SCALE, ROUNDING).multiply(BigDecimal.valueOf(30));
    }

    private BigDecimal getgrossSalary(Contract contract, BigDecimal baseSalary, BigDecimal payrollDeduction, int year,
            int month) {

        if (contract.getEmploymentType() == EmploymentType.HOURLY) {
            return baseSalary.add(payrollDeduction);
        } else {
            return contract.getSalary().add(payrollDeduction);
        }

    }

    private BigDecimal calculateHourlyRate(Contract contract) {
        BigDecimal monthlyHours = getTotalMonthlyHours(contract);
        return contract.getSalary().divide(monthlyHours, SCALE, ROUNDING);
    }

    private BigDecimal getExtraWorkedHoursCount(Contract contract, int year, int month) {
        List<Job> monthlyJobs = getMonthlyJobs(contract, year, month);

        return getWorkedHours(monthlyJobs, year, month);
    }

    private BigDecimal getExtraHoursAmount(Contract contract, int year, int month) {
        if (contract.getEmploymentType() == EmploymentType.HOURLY) {

            return BigDecimal.ZERO;
        }
        BigDecimal actualWorked = getExtraWorkedHoursCount(contract, year, month);
        BigDecimal expected = getTotalMonthlyHours(contract);

        BigDecimal extra = actualWorked.subtract(expected);
        if (extra.compareTo(BigDecimal.ZERO) > 0) {
            return extra.multiply(calculateHourlyRate(contract));
        }

        return BigDecimal.ZERO;

    }

    private BigDecimal safe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private BigDecimal claculateNetSalary(BigDecimal grossSalary, BigDecimal extraHours, BigDecimal payrollDeductions,
            BigDecimal otherDeductions, BigDecimal other, BigDecimal transportation, BigDecimal servicePlus,
            BigDecimal gratuities) {
        BigDecimal deductions = safe(payrollDeductions).add(safe(otherDeductions));
        BigDecimal additions = grossSalary.add(safe(extraHours)).add(safe(other)).add(safe(transportation))
                .add(safe(servicePlus))
                .add(safe(gratuities));
        return additions.subtract(deductions);
    }

    @Override
    public List<PayslipDetailsDto> findAllByContractId(int contractId) {
        List<Payslip> payslips = payslipRepository.findByContractId(contractId);
        List<PayslipDetailsDto> details = new ArrayList<>();

        for (Payslip payslip : payslips) {
            Contract contract = payslip.getContract();
            int sinceYear = contract.getSince().getYear();
            String sinceMonth = getMonthNameInSpanish(contract.getSince());
            String since = sinceMonth + " " + String.valueOf(sinceYear);
            String firstName = contract.getEmployee().getFirstName();
            String lastName = contract.getEmployee().getLastName();
            PayslipDetailsDto dto = detailsMapper.toDto(payslip);
            dto.setEmployeeName(firstName + " " + lastName);
            dto.setSince(since);
            details.add(dto);

        }

        return details;
    }

    @Override
    public Payslip findById(int id) {
        return payslipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payslip with id: " + id + " not found."));

    }

    @Override
    public Payslip buildPayslip(int contractId, CreatePayslipDto form) {
        Contract contract = contractService.findById(contractId);

        int year = form.getYear();
        int month = form.getMonth();
        int service = contract.getService();
        BigDecimal payrollDeduction = form.getPayrollDeduction();
        BigDecimal otherDeductions = form.getOtherDeductions();
        BigDecimal other = form.getOther();
        BigDecimal gratuities = form.getGratuities();
        String comments = form.getComments();
        BigDecimal grossSalary = getgrossSalary(contract,
                safe(form.getBaseSalary()), safe(form.getPayrollDeduction()), year,
                month);
        BigDecimal extraWorkedHours = form.getExtraWorkedHours();
        BigDecimal extraHours = form.getExtraHoursAmount();
        BigDecimal transportation = form.getTransportation();
        BigDecimal servicePlus = grossSalary.multiply(BigDecimal.valueOf(service)).divide(BigDecimal.valueOf(100),
                SCALE, ROUNDING);
        BigDecimal netSalary = claculateNetSalary(grossSalary, extraHours, payrollDeduction, otherDeductions, other,
                transportation, servicePlus, gratuities);

        Payslip payslip = new Payslip();
        payslip.setYear(year);
        payslip.setMonth(month);
        payslip.setService(service);
        payslip.setGrossSalary(grossSalary);
        payslip.setExtraWorkedHours(extraWorkedHours);
        payslip.setExtraHours(extraHours);
        payslip.setPayrollDeduction(payrollDeduction);
        payslip.setOtherDeductions(otherDeductions);
        payslip.setOther(other);
        payslip.setTransportation(transportation);
        payslip.setServicePlus(servicePlus);
        payslip.setGratuities(gratuities);
        payslip.setNetSalary(netSalary);
        payslip.setWorkedHours(extraWorkedHours);
        payslip.setComments(comments);
        payslip.setContract(contract);

        return payslip;
    }

    @Override
    public Payslip save(int contractId, CreatePayslipDto form) {
        Payslip payslip = buildPayslip(contractId, form);
        return payslipRepository.save(payslip);
    }

    @Override
    public PayslipDetailsDto getDetails(int id) {
        Payslip payslip = payslipRepository.findByIdWithContract(id)
                .orElseThrow(() -> new EntityNotFoundException("Payslip with id: " + id + " not found."));
        Contract contract = payslip.getContract();
        int sinceYear = contract.getSince().getYear();
        String sinceMonth = getMonthNameInSpanish(contract.getSince());
        String since = sinceMonth + " " + String.valueOf(sinceYear);
        String firstName = contract.getEmployee().getFirstName();
        String lastName = contract.getEmployee().getLastName();

        PayslipDetailsDto details = detailsMapper.toDto(payslip);

        details.setContractId(contract.getId());
        details.setEmployeeIdentificationNumber(contract.getEmployee().getIdentificationNumber());
        details.setEmployeeName(firstName + " " + lastName);
        details.setSince(since);
        return details;

    }

    @Override
    public CreatePayslipDto fillForm(int contractId, int year, int month) {
        Contract contract = contractService.findById(contractId);

        BigDecimal extraWorkedHours = getExtraWorkedHoursCount(contract, year, month);
        BigDecimal extraHours = getExtraHoursAmount(contract, year, month);
        BigDecimal transportation = getTransportation(contract, year, month);

        CreatePayslipDto dto = new CreatePayslipDto();
        dto.setBaseSalary(getPartial(getMonthlyJobs(contract, year, month), year, month));
        dto.setYear(year);
        dto.setMonth(month);
        dto.setExtraWorkedHours(extraWorkedHours);
        dto.setExtraHoursAmount(extraHours);
        dto.setTransportation(transportation);

        return dto;

    }

}
