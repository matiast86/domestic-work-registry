package com.springboot.domesticworkregistry.service.contract;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.ContractRepository;
import com.springboot.domesticworkregistry.dto.contract.CreateContractDto;
import com.springboot.domesticworkregistry.dto.contract.CreateContractWithEmployeeDto;
import com.springboot.domesticworkregistry.dto.employee.CreateEmployeeWithAddressDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Employee;
import com.springboot.domesticworkregistry.entities.Employer;
import com.springboot.domesticworkregistry.mapper.ContractMapper;
import com.springboot.domesticworkregistry.service.employee.EmployeeService;
import com.springboot.domesticworkregistry.service.employer.EmployerService;
import com.springboot.domesticworkregistry.service.job.JobService;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final EmployerService employerService;
    private final EmployeeService employeeService;
    private final JobService jobService;

    public ContractServiceImpl(ContractRepository contractRepository, ContractMapper contractMapper,
            EmployerService employerService,
            EmployeeService employeeService, JobService jobService) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.employerService = employerService;
        this.employeeService = employeeService;
        this.jobService = jobService;
    }

    @Override
    public List<Contract> findAll() {
        List<Contract> contracts = contractRepository.findAll();
        return contracts;
    }

    @Override
    public Contract save(String employerEmail, CreateContractWithEmployeeDto form) {
        Employer employer = employerService.findByEmail(employerEmail);
        CreateEmployeeWithAddressDto dto = form.getEmployeeDto();
        Employee employee = employeeService.save(dto);
        Contract newContract = contractMapper.toContract(form.getContract());
        employer.addContract(newContract);
        employee.addContract(newContract);
        return contractRepository.save(newContract);

    }

    @Override
    public Contract findById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

}
