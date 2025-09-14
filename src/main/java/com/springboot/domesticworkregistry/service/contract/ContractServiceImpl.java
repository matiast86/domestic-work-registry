package com.springboot.domesticworkregistry.service.contract;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.ContractRepository;
import com.springboot.domesticworkregistry.dto.address.CreateAddressDto;
import com.springboot.domesticworkregistry.dto.contract.ContractDetailsWithemployeeDto;
import com.springboot.domesticworkregistry.dto.contract.CreateContractDto;
import com.springboot.domesticworkregistry.dto.contract.CreateEmployeeFormDto;
import com.springboot.domesticworkregistry.dto.employee.CreateEmployeeDto;
import com.springboot.domesticworkregistry.dto.employee.CreateEmployeeWithAddressDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Employee;
import com.springboot.domesticworkregistry.entities.Employer;
import com.springboot.domesticworkregistry.exceptions.EntityNotFoundException;
import com.springboot.domesticworkregistry.mapper.ContractDetailsMapper;
import com.springboot.domesticworkregistry.mapper.ContractMapper;
import com.springboot.domesticworkregistry.service.employee.EmployeeService;
import com.springboot.domesticworkregistry.service.employer.EmployerService;

import jakarta.transaction.Transactional;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final EmployerService employerService;
    private final EmployeeService employeeService;
    private final ContractDetailsMapper contractDetailsMapper;

    public ContractServiceImpl(ContractRepository contractRepository, ContractMapper contractMapper,
            EmployerService employerService,
            EmployeeService employeeService, ContractDetailsMapper contractDetailsMapper) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.employerService = employerService;
        this.employeeService = employeeService;
        this.contractDetailsMapper = contractDetailsMapper;

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
        Employer employer = employerService.findByEmail(employerEmail);

        CreateEmployeeDto employeeDto = new CreateEmployeeDto(
                form.getFirstName(),
                form.getLastName(),
                form.getEmail(),
                form.getCuil(),
                form.getPhone()

        );

        CreateAddressDto addressDto = new CreateAddressDto(
                form.getStreet(),
                form.getNumber(),
                form.getCity(),
                form.getPostalCode(),
                form.getCountry());

        CreateEmployeeWithAddressDto employeeWithAddressDto = new CreateEmployeeWithAddressDto(employeeDto, addressDto);

        CreateContractDto contractDto = new CreateContractDto(
                form.getJobType(),
                form.getEmploymentType(),
                form.getSalary());

        Employee employee = employeeService.save(employeeWithAddressDto);
        Contract newContract = contractMapper.toContract(contractDto);
        newContract.setName(employer.getLastName().toUpperCase() + "-" + employee.getLastName().toUpperCase());
        newContract.setStartDate(new Date());
        newContract.setActive(true);
        employer.addContract(newContract);
        employee.addContract(newContract);
        return contractRepository.save(newContract);

    }

    @Override
    public Contract findById(int id) {
        Optional<Contract> result = contractRepository.findById(id);
        Contract theContract = null;

        if (result.isPresent()) {
            theContract = result.get();
        } else {
            throw new RuntimeException("Contract not found");
        }

        return theContract;
    }

    @Transactional
    @Override
    public ContractDetailsWithemployeeDto findByIdWithEmployee(int id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract with id " + id + " not found"));

        return contractDetailsMapper.toDto(contract);
    }

}
