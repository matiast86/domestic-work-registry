package com.springboot.domesticworkregistry.service.payslip;

import java.util.List;

import com.springboot.domesticworkregistry.dto.payslip.CreatePayslipDto;
import com.springboot.domesticworkregistry.dto.payslip.PayslipDetailsDto;
import com.springboot.domesticworkregistry.entities.Payslip;

public interface PayslipService {
    List<PayslipDetailsDto> findAllByContractId(int contractId);

    Payslip findById(int id);

    Payslip buildPayslip(int contractId, CreatePayslipDto form);

    Payslip save(int contractId, CreatePayslipDto form);

    PayslipDetailsDto getDetails(int id);

    CreatePayslipDto fillForm(int contractId, int year, int month);

}
