package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.springboot.domesticworkregistry.dto.contract.ContractDetailsWithemployeeDto;
import com.springboot.domesticworkregistry.entities.Contract;

@Mapper(componentModel = "spring")
public interface ContractDetailsMapper {
    @Mapping(source = "employee.firstName", target = "firstName")
    @Mapping(source = "employee.lastName", target = "lastName")
    @Mapping(source = "employee.email", target = "email")
    @Mapping(source = "employee.cuil", target = "cuil")
    @Mapping(source = "employee.phone", target = "phone")
    @Mapping(source = "employee.homeAddress.street", target = "street")
    @Mapping(source = "employee.homeAddress.number", target = "number")
    @Mapping(source = "employee.homeAddress.city", target = "city")
    @Mapping(source = "employee.homeAddress.postalCode", target = "postalCode")
    @Mapping(source = "employee.homeAddress.country", target = "country")
    ContractDetailsWithemployeeDto toDto(Contract contract);
}
