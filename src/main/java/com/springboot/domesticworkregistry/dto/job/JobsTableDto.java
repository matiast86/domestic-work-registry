package com.springboot.domesticworkregistry.dto.job;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobsTableDto {
    private int contractId;
    private int year;
    private String month;
    private Double workedHours;
    private BigDecimal hourlyFee;
    private BigDecimal subtotal;
    private BigDecimal transportationFee;
    private BigDecimal total;

}
