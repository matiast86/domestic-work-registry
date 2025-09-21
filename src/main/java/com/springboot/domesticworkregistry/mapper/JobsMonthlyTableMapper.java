package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.springboot.domesticworkregistry.dto.job.JobsMonthlyTableDto;
import com.springboot.domesticworkregistry.entities.Job;

@Mapper(componentModel = "spring")
public interface JobsMonthlyTableMapper {
    @Mapping(source = "id", target = "jobId")
    JobsMonthlyTableDto toDo(Job job);
}
