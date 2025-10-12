package com.springboot.domesticworkregistry.service.payslip;

import java.util.List;

import com.springboot.domesticworkregistry.dto.payslip.CreatePayslipDto;
import com.springboot.domesticworkregistry.entities.Payslip;

public interface PayslipService {
    List<Payslip> findAll();

    Payslip findById(int id);

    Payslip save(int contractId, CreatePayslipDto form);
}
