package com.springboot.domesticworkregistry.service.contract;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.ContractRepository;
import com.springboot.domesticworkregistry.dto.contract.ContractDetailsWithemployeeDto;
import com.springboot.domesticworkregistry.dto.contract.CreateContractDto;
import com.springboot.domesticworkregistry.dto.contract.CreateEmployeeFormDto;
import com.springboot.domesticworkregistry.dto.user.RegisterUserEmployeeDto;
import com.springboot.domesticworkregistry.entities.Address;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Schedule;
import com.springboot.domesticworkregistry.entities.User;
import com.springboot.domesticworkregistry.enums.Role;
import com.springboot.domesticworkregistry.exceptions.EntityNotFoundException;
import com.springboot.domesticworkregistry.mapper.ContractDetailsMapper;
import com.springboot.domesticworkregistry.mapper.ContractMapper;
import com.springboot.domesticworkregistry.mapper.RegisterEmployeeDtoMapper;
import com.springboot.domesticworkregistry.service.schedule.ScheduleService;
import com.springboot.domesticworkregistry.service.user.UserService;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final UserService userService;
    private final ScheduleService scheduleService;
    private final ContractDetailsMapper contractDetailsMapper;
    private final RegisterEmployeeDtoMapper employeeDtoMapper;

    public ContractServiceImpl(ContractRepository contractRepository, ContractMapper contractMapper,
            UserService userService, ScheduleService scheduleService, ContractDetailsMapper contractDetailsMapper,
            RegisterEmployeeDtoMapper employeeDtoMapper) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.userService = userService;
        this.scheduleService = scheduleService;
        this.contractDetailsMapper = contractDetailsMapper;
        this.employeeDtoMapper = employeeDtoMapper;

    }

    @Override
    public List<Contract> findAll() {
        List<Contract> contracts = contractRepository.findAll();
        return contracts;
    }

    // Fast query
    @Override
    public List<Contract> findAllByEmployer(String employerId) {
        return contractRepository.findSummaryByEmployerId(employerId);
    }

    @Override
    public Contract save(String employerEmail, CreateEmployeeFormDto form) {
        User employer = userService.findByEmail(employerEmail);

        // check if active contract already exists
        contractRepository.findByEmployerIdAndEmployeeEmailAndActiveTrue(employer.getId(), form.getEmail())
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            "There is already an active contract between employer " + employer.getEmail() +
                                    " and employee " + form.getEmail());
                });

        // Build schedule
        Schedule schedule = new Schedule();
        schedule.setEntries(form.getEntries());

        // Build contract from DTO
        CreateContractDto contractDto = new CreateContractDto(
                form.getSince(),
                form.getJobType(),
                form.getEmploymentType(),
                form.getSalary(),
                schedule);

        Contract newContract = contractMapper.toContract(contractDto);

        // Find or create employee
        User employee;
        try {
            employee = userService.findByEmail(form.getEmail());
            if (!employee.getRoles().contains(Role.EMPLOYEE)) {
                employee.getRoles().add(Role.EMPLOYEE);
            }
        } catch (EntityNotFoundException e) {
            RegisterUserEmployeeDto employeeDto = employeeDtoMapper.toDto(form);
            employee = userService.registerEmployee(employeeDto);
        }

        // Common setup
        schedule.setContract(newContract);

        newContract.setEmployer(employer);
        employer.addEmployerContract(newContract);

        newContract.setEmployee(employee);
        employee.addEmployeeContract(newContract);

        newContract.setName(employer.getLastName().toUpperCase() + "-" + employee.getLastName().toUpperCase());
        newContract.setStartDate(LocalDate.now());
        newContract.setActive(true);

        return contractRepository.save(newContract);
    }

    @Override
    public Contract findById(int id) {
        Contract contract = contractRepository.findDetailById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract with id " + id + " not found"));

        return contract;
    }

    @Override
    public ContractDetailsWithemployeeDto findByIdWithEmployee(int id) {
        Contract contract = contractRepository.findDetailById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract with id " + id + " not found"));

        return contractDetailsMapper.toDto(contract);
    }

    @Override
    public Contract update(int id, ContractDetailsWithemployeeDto form) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract with id " + id + " not found"));

        User employee = contract.getEmployee();
        User employer = contract.getEmployer();

        // Let MapStruct handle contract fields
        contractDetailsMapper.updateContractFromDto(form, contract);

        // Update employee fields
        employee.setFirstName(form.getFirstName());
        employee.setLastName(form.getLastName());
        employee.setEmail(form.getEmail());
        employee.setBirthDate(form.getBirthdate());
        employee.setIdentificationNumber(form.getIdentificationNumber());
        employee.setPhone(form.getPhone());

        // Update employee address
        Address address = employee.getAddress();
        address.setStreet(form.getStreet());
        address.setNumber(form.getNumber());
        address.setApartment(form.getApartment());
        address.setCity(form.getCity());
        address.setPostalCode(form.getPostalCode());
        address.setCountry(form.getCountry());

        // Update contract name
        contract.setName(employer.getLastName().toUpperCase() + "-" + employee.getLastName().toUpperCase());

        return contractRepository.save(contract);
    }

}
