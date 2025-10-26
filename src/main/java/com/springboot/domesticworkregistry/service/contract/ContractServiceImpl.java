package com.springboot.domesticworkregistry.service.contract;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.ContractRepository;
import com.springboot.domesticworkregistry.dto.contract.ContractDetailsWithemployeeDto;
import com.springboot.domesticworkregistry.dto.contract.ContractMapper;
import com.springboot.domesticworkregistry.dto.contract.CreateEmployeeFormDto;
import com.springboot.domesticworkregistry.dto.schedule_entry.ScheduleEntryDto;
import com.springboot.domesticworkregistry.dto.user.RegisterUserEmployeeDto;
import com.springboot.domesticworkregistry.entities.Address;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.User;
import com.springboot.domesticworkregistry.enums.Role;
import com.springboot.domesticworkregistry.exceptions.EntityNotFoundException;
import com.springboot.domesticworkregistry.mapper.ContractDetailsMapper;
import com.springboot.domesticworkregistry.mapper.RegisterEmployeeDtoMapper;
import com.springboot.domesticworkregistry.service.dataCollection.DataCollectionService;
import com.springboot.domesticworkregistry.service.user.UserService;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final UserService userService;
    private final ContractDetailsMapper contractDetailsMapper;
    private final RegisterEmployeeDtoMapper employeeDtoMapper;
    private final DataCollectionService dataCollectionService;

    public ContractServiceImpl(ContractRepository contractRepository, ContractMapper contractMapper,
            UserService userService, ContractDetailsMapper contractDetailsMapper,
            RegisterEmployeeDtoMapper employeeDtoMapper, DataCollectionService dataCollectionService) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.userService = userService;
        this.contractDetailsMapper = contractDetailsMapper;
        this.employeeDtoMapper = employeeDtoMapper;
        this.dataCollectionService = dataCollectionService;

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

        Contract newContract = contractMapper.fromForm(form, employer, employee);
        newContract.setWorkAddress(employer.getAddress());
        newContract.setExpectedMonthlyHours(dataCollectionService.getTotalMonthlyHours(newContract));

        return contractRepository.save(newContract);
    }

    @Override
    public Contract findById(int id) {
        Contract contract = contractRepository.findDetailById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract with id " + id + " not found"));

        return contract;
    }

    @Override
    public ContractDetailsWithemployeeDto findByIdWithEmployee(int id, String currentUserId) {
        Contract contract = contractRepository.findDetailById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract with id " + id + " not found"));

        if (!contract.getEmployer().getId().equals(currentUserId) &&
                !contract.getEmployee().getId().equals(currentUserId)) {
            throw new AccessDeniedException("You are not allowed to view this contract");
        }

        ContractDetailsWithemployeeDto details = contractDetailsMapper.toDto(contract);
        List<ScheduleEntryDto> entries = contract.getSchedule().getEntries().stream()
                .map(entry -> new ScheduleEntryDto(entry.getDayOfWeek(), entry.getStartTime(), entry.getEndTime()))
                .toList();

        details.setEntries(entries);
        return details;
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
        address.setState(form.getState());
        address.setCity(form.getCity());
        address.setPostalCode(form.getPostalCode());
        address.setCountry(form.getCountry());

        // Update contract name
        contract.setName(employer.getLastName().toUpperCase() + "-" + employee.getLastName().toUpperCase());

        return contractRepository.save(contract);
    }

    @Override
    public Contract findByIdWithSchedule(int id) {
        return contractRepository.findByIdWithSchedule(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract with id " + id + " not found"));
    }

    @Override
    public Contract findByIdWithPayslips(int id) {
        return contractRepository.findByIdWithPayslips(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract with id " + id + " not found"));
    }

}
