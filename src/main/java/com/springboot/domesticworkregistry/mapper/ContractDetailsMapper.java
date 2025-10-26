package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.springboot.domesticworkregistry.dto.contract.ContractDetailsWithemployeeDto;
import com.springboot.domesticworkregistry.entities.Contract;

@Mapper(componentModel = "spring")
public interface ContractDetailsMapper {

    // Existing mapping: Contract -> DTO

    ContractDetailsWithemployeeDto toDto(Contract contract);

    void updateContractFromDto(ContractDetailsWithemployeeDto dto, @MappingTarget Contract contract);

    // Similarly, for Employee and Address if needed
}
