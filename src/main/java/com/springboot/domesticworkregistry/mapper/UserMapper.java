package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.springboot.domesticworkregistry.dto.user.RegisterUserDto;
import com.springboot.domesticworkregistry.dto.user.RegisterUserEmployeeDto;
import com.springboot.domesticworkregistry.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "active", constant = "true")
    User toEmployer(RegisterUserDto form);
    User toEmployee(RegisterUserEmployeeDto form);

}
