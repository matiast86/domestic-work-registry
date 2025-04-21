package com.springboot.domesticworkregistry.dto.contract;

import java.util.Date;

import com.springboot.domesticworkregistry.entities.Employee;
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
    public Date startDate;

    public Date endDate;

    @NotNull(message = "is required")
    private JobType jobType;

    @NotNull(message = "is required")
    private EmploymentType employmentType;

    private Double salary;


}
