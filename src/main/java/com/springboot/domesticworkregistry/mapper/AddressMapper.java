package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;

import com.springboot.domesticworkregistry.dto.address.CreateAddressDto;
import com.springboot.domesticworkregistry.entities.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(CreateAddressDto dto);

}
