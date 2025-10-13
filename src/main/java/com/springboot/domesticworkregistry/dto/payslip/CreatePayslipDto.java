package com.springboot.domesticworkregistry.dto.payslip;

import java.math.BigDecimal;
import java.time.YearMonth;

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

    @NotNull(message = "is required")
    private YearMonth period;

    @NotNull(message = "is required")
    private BigDecimal payrollDeduction;

    @PositiveOrZero
    private BigDecimal otherDeductions;

    @PositiveOrZero
    private BigDecimal other;

    @PositiveOrZero
    private BigDecimal gratuities;

    @Size(max = 255, message = "Comments must be at most 255 characters")
    private String comments;

    public int getYear() {
        return period != null ? period.getYear() : 0;
    }

    public int getMonth() {
        return period != null ? period.getMonthValue() : 0;
    }

}
