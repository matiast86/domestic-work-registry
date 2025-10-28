package com.springboot.domesticworkregistry.dto.user;

import org.springframework.stereotype.Component;

import com.springboot.domesticworkregistry.dto.contract.CreateEmployeeFormDto;

@Component
public class RegisterEmployeeDtoMapper {
    public RegisterUserEmployeeDto toDto(CreateEmployeeFormDto form) {
        RegisterUserEmployeeDto dto = new RegisterUserEmployeeDto();
        dto.setFirstName(form.getFirstName());
        dto.setLastName(form.getLastName());
        dto.setEmail(form.getEmail());
        dto.setBirthDate(form.getBirthDate());
        dto.setIdentificationNumber(form.getIdentificationNumber());
        dto.setPhone(form.getPhone());
        dto.setStreet(form.getStreet());
        dto.setNumber(form.getNumber());
        dto.setApartment(form.getApartment());
        dto.setState(form.getState());
        dto.setPostalCode(form.getPostalCode());
        dto.setCountry(form.getCountry());

        return dto;
    }
}
