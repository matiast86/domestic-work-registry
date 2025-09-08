package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;

import com.springboot.domesticworkregistry.dto.contract.CreateContractDto;
import com.springboot.domesticworkregistry.entities.Contract;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    Contract toContract(CreateContractDto dto);

}
