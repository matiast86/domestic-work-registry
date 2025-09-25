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

import jakarta.transaction.Transactional;

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

    @Override
    public List<Contract> findAllByEmployer(String employerId) {
        return contractRepository.findAllByEmployerId(employerId);
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

        Schedule schedule = new Schedule();
        schedule.setEntries(form.getEntries());

        CreateContractDto contractDto = new CreateContractDto(
                form.getSince(),
                form.getJobType(),
                form.getEmploymentType(),
                form.getSalary(),
                schedule);

        Contract newContract = contractMapper.toContract(contractDto);
        User existingUser = userService.findByEmail(form.getEmail());
        if (existingUser != null && existingUser.getRoles().contains(Role.EMPLOYEE)) {
            schedule.setContract(newContract);
            newContract.setEmployer(employer);
            employer.addEmployerContract(newContract);
            newContract.setEmployee(existingUser);
            existingUser.addEmployeeContract(newContract);
            newContract.setName(employer.getLastName().toUpperCase() + "-" + existingUser.getLastName().toUpperCase());
            newContract.setStartDate(LocalDate.now());
            newContract.setActive(true);
            return newContract;
        }
        if (existingUser != null && !existingUser.getRoles().contains(Role.EMPLOYEE)) {
            existingUser.getRoles().add(Role.EMPLOYEE);
            schedule.setContract(newContract);
            newContract.setEmployer(employer);
            employer.addEmployerContract(newContract);
            newContract.setEmployee(existingUser);
            existingUser.addEmployeeContract(newContract);
            newContract.setName(employer.getLastName().toUpperCase() + "-" + existingUser.getLastName().toUpperCase());
            newContract.setStartDate(LocalDate.now());
            newContract.setActive(true);
            return newContract;
        }
        RegisterUserEmployeeDto employeeDto = employeeDtoMapper.toDto(form);
        User employee = userService.registerEmployee(employeeDto);

        schedule.setContract(newContract);
        newContract.setName(employer.getLastName().toUpperCase() + "-" + employee.getLastName().toUpperCase());
        newContract.setStartDate(LocalDate.now());
        newContract.setActive(true);
        employer.addEmployerContract(newContract);
        newContract.setEmployer(employer);
        employee.addEmployeeContract(newContract);
        newContract.setEmployee(employee);

        return contractRepository.save(newContract);

    }

    @Override
    public Contract findById(int id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract with id " + id + " not found"));

        return contract;
    }

    @Transactional
    @Override
    public ContractDetailsWithemployeeDto findByIdWithEmployee(int id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract with id " + id + " not found"));

        return contractDetailsMapper.toDto(contract);
    }

    @Override
    public Contract update(int id, ContractDetailsWithemployeeDto form) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract with id " + id + " not found"));

        User employee = contract.getEmployee(); // direct, no iterator
        User employer = contract.getEmployer();

        // update employee fields
        employee.setFirstName(form.getFirstName());
        employee.setLastName(form.getLastName());
        employee.setEmail(form.getEmail());
        employee.setBirthDate(form.getBirthdate());
        employee.setIdentificationNumber(form.getIdentificationNumber());
        employee.setPhone(form.getPhone());

        // update address
        Address address = employee.getAddress();
        address.setStreet(form.getStreet());
        address.setNumber(form.getNumber());
        address.setApartment(form.getApartment());
        address.setCity(form.getCity());
        address.setPostalCode(form.getPostalCode());
        address.setCountry(form.getCountry());

        // update contract details
        contract.setJobType(form.getJobType());
        contract.setEmploymentType(form.getEmploymentType());
        contract.setSalary(form.getSalary());
        contract.setName(employer.getLastName().toUpperCase() + "-" + employee.getLastName().toUpperCase());

        return contractRepository.save(contract);
    }

}
