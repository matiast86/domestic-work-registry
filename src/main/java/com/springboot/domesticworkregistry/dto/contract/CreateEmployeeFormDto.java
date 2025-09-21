package com.springboot.domesticworkregistry.dto.contract;

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
public class CreateEmployeeFormDto {

    @NotNull(message = "is requied")
    @Size(min = 1, message = "is required")
    private String firstName;

    @NotNull(message = "is requied")
    @Size(min = 1, message = "is required")
    private String lastName;

    @NotNull(message = "is requied")
    @Email
    private String email;

    @NotNull(message = "is required")
    private String cuil;

    @Size(min = 1)
    private String phone;

    @NotNull(message = "is required")
    private JobType jobType;

    @NotNull(message = "is required")
    private EmploymentType employmentType;

    private Double salary;

    @NotNull(message = "is required")
    private String street;

    @NotNull(message = "is required")
    private String number;

    @NotNull(message = "is required")
    private String city;

    @NotNull(message = "is required")
    private String postalCode;

    @NotNull(message = "is required")
    private String country;

    private int contractId;

}
