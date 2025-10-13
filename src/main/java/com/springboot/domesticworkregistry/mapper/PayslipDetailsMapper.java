package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.springboot.domesticworkregistry.dto.payslip.PayslipDetailsDto;
import com.springboot.domesticworkregistry.entities.Payslip;

@Mapper(componentModel = "spring")
public interface PayslipDetailsMapper {

    @Mapping(source = "id", target = "payslipId")
    @Mapping(source = "contract.id", target = "contractId")
    @Mapping(source = "contract.employee.identificationNumber", target = "employeeIdentificationNumber")
    PayslipDetailsDto toDto(Payslip payslip);
}
