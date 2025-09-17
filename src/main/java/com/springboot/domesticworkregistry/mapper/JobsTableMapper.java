package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;

import com.springboot.domesticworkregistry.dto.job.JobsTableDto;

@Mapper(componentModel = "spring")
public interface JobsTableMapper {

    JobsTableDto toDo(JobsTableDto dto);

}
