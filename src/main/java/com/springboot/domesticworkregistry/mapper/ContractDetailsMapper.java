package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.springboot.domesticworkregistry.dto.contract.ContractDetailsWithemployeeDto;
import com.springboot.domesticworkregistry.entities.Contract;

@Mapper(componentModel = "spring")
public interface ContractDetailsMapper {

    // Existing mapping: Contract -> DTO
    @Mapping(source = "employee.firstName", target = "firstName")
    @Mapping(source = "employee.lastName", target = "lastName")
    @Mapping(source = "employee.email", target = "email")
    @Mapping(source = "employee.birthDate", target = "birthdate")
    @Mapping(source = "employee.identificationNumber", target = "identificationNumber")
    @Mapping(source = "employee.phone", target = "phone")
    @Mapping(source = "id", target = "contractId")
    @Mapping(source = "employee.address.street", target = "street")
    @Mapping(source = "employee.address.number", target = "number")
    @Mapping(source = "employee.address.apartment", target = "apartment")
    @Mapping(source = "employee.address.city", target = "city")
    @Mapping(source = "employee.address.state", target = "state")
    @Mapping(source = "employee.address.postalCode", target = "postalCode")
    @Mapping(source = "employee.address.country", target = "country")
    ContractDetailsWithemployeeDto toDto(Contract contract);

    // New mapping: DTO -> existing Contract (update in place)
    @Mapping(target = "id", ignore = true) // don't overwrite primary key
    @Mapping(target = "employer", ignore = true) // handled manually
    @Mapping(target = "employee", ignore = true) // handled manually
    @Mapping(target = "jobs", ignore = true) // leave jobs unchanged
    @Mapping(target = "schedule", ignore = true) // leave schedule unchanged
    @Mapping(target = "workAddress", ignore = true) // leave workAddress unchanged
    void updateContractFromDto(ContractDetailsWithemployeeDto dto, @MappingTarget Contract contract);

    // Similarly, for Employee and Address if needed
}
