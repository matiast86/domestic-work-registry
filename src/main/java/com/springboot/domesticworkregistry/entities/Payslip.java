package com.springboot.domesticworkregistry.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.springboot.domesticworkregistry.enums.PayslipStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payslips")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payslip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "year")
    private int year;

    @Column(name = "month")
    private int month;

    @Column(name = "service", nullable = true, precision = 10, scale = 2)
    private int service;

    @Column(name = "gross_salary", precision = 10, scale = 2)
    private BigDecimal grossSalary;

    @Column(name = "extra_worked_hours")
    private BigDecimal extraWorkedHours;

    @Column(name = "extra_hours", precision = 10, scale = 2)
    private BigDecimal extraHours;

    @Column(name = "payroll_deduction", nullable = true, precision = 10, scale = 2)
    private BigDecimal payrollDeduction;

    @Column(name = "other_deductions", nullable = true, precision = 10, scale = 2)
    private BigDecimal otherDeductions;

    @Column(name = "other", nullable = true, precision = 10, scale = 2)
    private BigDecimal other;

    @Column(name = "transportation", nullable = true, precision = 10, scale = 2)
    private BigDecimal transportation;

    @Column(name = "service_plus", nullable = true, precision = 10, scale = 2)
    private BigDecimal servicePlus;

    @Column(name = "gratuities", nullable = true, precision = 10, scale = 2)
    private BigDecimal gratuities;

    @Column(name = "net_salary", precision = 10, scale = 2)
    private BigDecimal netSalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PayslipStatus status = PayslipStatus.DRAFT;

    @Column(name = "generated_at")
    private LocalDate generatedAt = LocalDate.now();

    @Column(name = "worked_hours")
    private BigDecimal workedHours;

    @Column(name = "comments", nullable = true)
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

}
