package com.springboot.domesticworkregistry.dto.contract;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.springboot.domesticworkregistry.entities.Schedule;
import com.springboot.domesticworkregistry.enums.EmploymentType;
import com.springboot.domesticworkregistry.enums.JobType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDetailsWithemployeeDto {
    private int contractId;

    @NotBlank(message = "is required")
    private String firstName;

    @NotBlank(message = "is required")
    private String lastName;

    @NotNull(message = "is required")
    @Email
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "is required")
    private LocalDate birthdate;

    @NotNull(message = "is required")
    private String identificationNumber;

    @NotBlank(message = "is required")
    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "is required")
    private LocalDate since;

    @NotNull(message = "is required")
    private JobType jobType;

    @NotNull(message = "is required")
    private EmploymentType employmentType;

    @NotNull(message = "is required")
    private BigDecimal salary;

    @NotNull(message = "is required")
    private Schedule schedule;

    @NotNull(message = "is required")
    private String street;

    @NotNull(message = "is required")
    private String number;

    private String apartment;

    @NotNull(message = "is required")
    private String state;

    private String city;

    @NotNull(message = "is required")
    private String postalCode;

    @NotNull(message = "is required")
    private String country;

}
