package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;

import com.springboot.domesticworkregistry.dto.job.JobsMonthlyTableDto;
import com.springboot.domesticworkregistry.entities.Job;

@Mapper(componentModel = "spring")
public interface JobsMonthlyTableMapper {

    JobsMonthlyTableDto toDo(Job job);
}