package com.springboot.domesticworkregistry.dto.contract;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.springboot.domesticworkregistry.dto.schedule_entry.ScheduleEntryDto;
import com.springboot.domesticworkregistry.entities.Schedule;
import com.springboot.domesticworkregistry.enums.EmploymentType;
import com.springboot.domesticworkregistry.enums.JobType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDetailsWithemployeeDto {
    private int contractId;

    private String firstName;

    private String lastName;

    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    private String identificationNumber;

    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate since;

    private int service;

    private JobType jobType;

    private EmploymentType employmentType;

    private BigDecimal salary;

    private List<ScheduleEntryDto> entries;

    private String street;

    private String number;

    private String apartment;

    private String state;

    private String city;

    private String postalCode;

    private String country;

    public int getService() {
        return since != null ? Period.between(since, LocalDate.now()).getYears() : 0;
    }

}
