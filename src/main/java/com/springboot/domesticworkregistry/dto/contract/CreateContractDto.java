package com.springboot.domesticworkregistry.dto.contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.springboot.domesticworkregistry.entities.ScheduleEntry;
import com.springboot.domesticworkregistry.enums.EmploymentType;
import com.springboot.domesticworkregistry.enums.JobType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateContractDto {

    @NotNull(message = "is required")
    private LocalDate since;

    @NotNull(message = "is required")
    private JobType jobType;

    @NotNull(message = "is required")
    private EmploymentType employmentType;

    @NotNull(message = "is required")
    private BigDecimal salary;

    @NotNull(message = "is required")
    private List<ScheduleEntry> entries;

}
