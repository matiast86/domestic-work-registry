package com.springboot.domesticworkregistry.dto.job;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobsMonthlyReportDto {
    private int year;
    private int month;
    private String monthName;
    List<JobsMonthlyTableDto> rows;
    JobsTotalsDto totals;
}
