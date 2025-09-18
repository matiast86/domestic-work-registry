package com.springboot.domesticworkregistry.dto.job;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobsReportDto {
    private List<JobsTableDto> monthlyRows;
    private List<JobsTotalsDto> totals;

}
