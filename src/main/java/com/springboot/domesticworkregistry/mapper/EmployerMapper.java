package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.springboot.domesticworkregistry.dto.employer.RegisterEmployerDto;
import com.springboot.domesticworkregistry.entities.Employer;



@Mapper(componentModel = "spring")
public interface EmployerMapper {
    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", expression = "java(com.springboot.domesticworkregistry.enums.Role.EMPLOYER)")
    @Mapping(target = "active", constant = "true")
    Employer toEmployer(RegisterEmployerDto dto);


}
