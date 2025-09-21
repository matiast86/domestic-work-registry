package com.springboot.domesticworkregistry.dto.job;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobsMonthlyTableDto {
    private int jobId;
    private LocalDate date;
    private int year;
    private String month;
    private Double workedHours;
    private BigDecimal hourlyRate;
    private BigDecimal partialFee;
    private BigDecimal transportationFee;
    private BigDecimal totalFee;

}
