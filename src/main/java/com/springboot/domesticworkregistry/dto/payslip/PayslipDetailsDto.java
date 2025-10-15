package com.springboot.domesticworkregistry.dto.payslip;

import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayslipDetailsDto {
    public int payslipId;
    public int contractId;

    private int year;
    private int month;

    private String employeeName;
    private String employeeIdentificationNumber;
    private int service;

    private String since;

    private BigDecimal grossSalary;
    private BigDecimal extraHours;
    private BigDecimal servicePlus;
    private BigDecimal transportation;
    private BigDecimal gratuities;
    private BigDecimal payrollDeduction;
    private BigDecimal otherDeductions;
    private BigDecimal other;
    private BigDecimal netSalary;

    private String comments;

    public String getMonthName() {
        Month monthValue = Month.of(month);
        return monthValue.getDisplayName(TextStyle.FULL, Locale.of("es", "AR")).toUpperCase();
    }

}
