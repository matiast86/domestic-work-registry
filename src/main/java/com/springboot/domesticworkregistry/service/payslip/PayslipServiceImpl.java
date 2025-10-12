package com.springboot.domesticworkregistry.service.payslip;

import java.util.List;

import com.springboot.domesticworkregistry.dao.PayslipRepository;
import com.springboot.domesticworkregistry.dto.payslip.CreatePayslipDto;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Payslip;
import com.springboot.domesticworkregistry.service.contract.ContractService;

import jakarta.persistence.EntityNotFoundException;

public class PayslipServiceImpl implements PayslipService {

    private final PayslipRepository payslipRepository;

    private final ContractService contractService;

    public PayslipServiceImpl(PayslipRepository payslipRepository, ContractService contractService) {
        this.payslipRepository = payslipRepository;
        this.contractService = contractService;

    }

    @Override
    public List<Payslip> findAll() {
        return payslipRepository.findAll();
    }

    @Override
    public Payslip findById(int id) {
        return payslipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payslip with id: " + id + " not found."));

    }

    @Override
    public Payslip save(int contractId, CreatePayslipDto form) {
        Contract contract = contractService.findById(contractId);
        return new Payslip();
    }

}
