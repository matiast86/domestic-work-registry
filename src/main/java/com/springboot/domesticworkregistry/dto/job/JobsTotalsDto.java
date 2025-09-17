package com.springboot.domesticworkregistry.dto.job;

import java.math.BigDecimal;

import com.springboot.domesticworkregistry.service.dataCollection.DataCollectionService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobsTotalsDto {
    private Double workedHoursTotal;
    private BigDecimal hourlyFeeTotal;
    private BigDecimal subtotalTotal;
    private BigDecimal transportationFeeTotal;
    private BigDecimal totalTotal;
    private DataCollectionService dataCollectionServiceTotal;
}
