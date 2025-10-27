package com.springboot.domesticworkregistry.dto.payslip;

import org.springframework.stereotype.Component;

import com.springboot.domesticworkregistry.entities.Payslip;

@Component
public class PayslipDetailsMapper {
    public PayslipDetailsDto toDto(Payslip payslip) {
        PayslipDetailsDto dto = new PayslipDetailsDto();
        dto.setPayslipId(payslip.getId());
        dto.setYear(payslip.getYear());
        dto.setMonth(payslip.getMonth());
        dto.setGrossSalary(payslip.getGrossSalary());
        dto.setExtraHours(payslip.getExtraHours());
        dto.setServicePlus(payslip.getServicePlus());
        dto.setTransportation(payslip.getTransportation());
        dto.setGratuities(payslip.getGratuities());
        dto.setPayrollDeduction(payslip.getPayrollDeduction());
        dto.setOther(payslip.getOther());
        dto.setNetSalary(payslip.getNetSalary());
        dto.setStatus(payslip.getStatus());
        dto.setGeneratedAt(payslip.getGeneratedAt());
        dto.setComments(payslip.getComments());

        return dto;
    }

}
