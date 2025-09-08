package com.springboot.domesticworkregistry.service.contract;

import java.util.List;
import java.util.Optional;

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
   

    public ContractServiceImpl(ContractRepository contractRepository, ContractMapper contractMapper,
            EmployerService employerService,
            EmployeeService employeeService) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.employerService = employerService;
        this.employeeService = employeeService;
      
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

}
