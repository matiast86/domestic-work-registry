package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;

import com.springboot.domesticworkregistry.dto.employee.CreateEmployeeDto;
import com.springboot.domesticworkregistry.entities.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    Employee toEmployee(CreateEmployeeDto dto);

}
