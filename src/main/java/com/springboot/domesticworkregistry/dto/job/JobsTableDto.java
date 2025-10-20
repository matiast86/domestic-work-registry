package com.springboot.domesticworkregistry.dto.job;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobsTableDto {
    private int contractId;
    private LocalDate date;
    private int year;
    private int monthValue;
    private String month;
    private BigDecimal workedHours;
    private BigDecimal hourlyFee; //average
    private BigDecimal subtotal;
    private BigDecimal transportationFee;
    private BigDecimal total;

}
