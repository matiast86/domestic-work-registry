package com.springboot.domesticworkregistry.dto.payslip;

import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePayslipDto {

    int year;

    int month;

    private BigDecimal baseSalary;

    @PositiveOrZero
    private BigDecimal extraWorkedHours;

    @PositiveOrZero
    private BigDecimal extraHoursAmount;

    @PositiveOrZero
    private BigDecimal transportation;

    @NotNull(message = "is required")
    private BigDecimal payrollDeduction;

    @PositiveOrZero
    private BigDecimal otherDeductions;

    @PositiveOrZero
    private BigDecimal other;

    @PositiveOrZero
    private BigDecimal gratuities;

    @PositiveOrZero
    private BigDecimal servicePlus;

    @Size(max = 255, message = "Comments must be at most 255 characters")
    private String comments;

    public String getMonthName() {
        Month monthName = Month.of(month);
        return monthName.getDisplayName(TextStyle.SHORT, Locale.of("es", "AR")).toUpperCase();
    }

    public String getPeriodString() {
        return getMonthName() + "-" + String.valueOf(year);
    }

}
