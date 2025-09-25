package com.springboot.domesticworkregistry.dto.contract;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.springboot.domesticworkregistry.entities.Schedule;
import com.springboot.domesticworkregistry.enums.EmploymentType;
import com.springboot.domesticworkregistry.enums.JobType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDetailsWithemployeeDto {
    private int contractId;

    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String firstName;

    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String lastName;

    @NotNull(message = "is required")
    @Email
    private String email;

    @NotNull(message = "is required")
    private LocalDate birthdate;

    @NotNull(message = "is required")
    private String identificationNumber;

    @NotNull(message = "is required")
    @Size(min = 1)
    private String phone;

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
    private String city;

    @NotNull(message = "is required")
    private String postalCode;

    @NotNull(message = "is required")
    private String country;

    public String getEmploymentTypeLabel() {
        switch (employmentType) {
            case Hourly:
                return "HOURLY";
            case Monthly:
                return "MONTHLY";

            default:
                return employmentType.name();
        }
    }

    public String getJobTypeLabel() {
        switch (jobType) {
            case Nanny:
                return "NANNY";
            case House_Keeper:
                return "HOUSE KEEPER";

            default:
                return jobType.name();
        }
    }

}
