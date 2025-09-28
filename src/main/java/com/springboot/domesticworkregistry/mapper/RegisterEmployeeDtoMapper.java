package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;

import com.springboot.domesticworkregistry.dto.contract.CreateEmployeeFormDto;
import com.springboot.domesticworkregistry.dto.user.RegisterUserEmployeeDto;

@Mapper(componentModel = "spring")
public interface RegisterEmployeeDtoMapper {
    RegisterUserEmployeeDto toDto(CreateEmployeeFormDto form);
}
