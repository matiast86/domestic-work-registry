package com.springboot.domesticworkregistry.dto.contract;

import com.springboot.domesticworkregistry.dto.employee.CreateEmployeeWithAddressDto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateContractWithEmployeeDto {

    @Valid
    public CreateContractDto contract;

    @Valid
    public CreateEmployeeWithAddressDto employeeDto;
}
