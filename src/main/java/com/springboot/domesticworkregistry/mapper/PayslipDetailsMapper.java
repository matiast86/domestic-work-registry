package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;

import com.springboot.domesticworkregistry.dto.payslip.PayslipDetailsDto;
import com.springboot.domesticworkregistry.entities.Payslip;

@Mapper(componentModel = "spring")
public interface PayslipDetailsMapper {

    PayslipDetailsDto toDto(Payslip payslip);
}
