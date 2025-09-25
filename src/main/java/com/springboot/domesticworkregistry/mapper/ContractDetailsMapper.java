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
    @Mapping(source = "employee.identificationNumber", target = "identificationNumber")
    @Mapping(source = "employee.phone", target = "phone")
    @Mapping(source = "id", target = "contractId")
    @Mapping(source = "employee.address.street", target = "street")
    @Mapping(source = "employee.address.number", target = "number")
    @Mapping(source = "employee.address.apartment", target = "apartment")
    @Mapping(source = "employee.address.city", target = "city")
    @Mapping(source = "employee.address.postalCode", target = "postalCode")
    @Mapping(source = "employee.address.country", target = "country")
    ContractDetailsWithemployeeDto toDto(Contract contract);
}
