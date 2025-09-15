package com.springboot.domesticworkregistry.dto.contract;

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

    private String cuil;

    private String phone;

    private JobType jobType;

    private EmploymentType employmentType;

    private Double salary;

    private String street;

    private String number;

    private String city;

    private String postalCode;

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
