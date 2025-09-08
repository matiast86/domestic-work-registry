package com.springboot.domesticworkregistry.dto.employee;

import com.springboot.domesticworkregistry.dto.address.CreateAddressDto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeWithAddressDto {

    @Valid
    private CreateEmployeeDto employee;

    @Valid
    private CreateAddressDto address;


}
