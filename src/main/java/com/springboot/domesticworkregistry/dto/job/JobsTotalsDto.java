package com.springboot.domesticworkregistry.dto.job;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobsTotalsDto {
    /**
     * For yearly totals, this field is the year.
     * For monthly totals, this field is just set to the year but not actually used.
     */
    private int year;
    private Double workedHoursTotal;
    private BigDecimal hourlyFeeTotal;
    private BigDecimal subtotalTotal;
    private BigDecimal transportationFeeTotal;
    private BigDecimal totalTotal;
}
