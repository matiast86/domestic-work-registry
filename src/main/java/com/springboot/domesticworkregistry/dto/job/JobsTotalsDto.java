package com.springboot.domesticworkregistry.dto.job;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobsTotalsDto {
    private int year;
    private Double workedHoursTotal;
    private BigDecimal hourlyFeeTotal;
    private BigDecimal subtotalTotal;
    private BigDecimal transportationFeeTotal;
    private BigDecimal totalTotal;
}
