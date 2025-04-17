package com.springboot.domesticworkregistry.mapper;

import org.mapstruct.Mapper;

import com.springboot.domesticworkregistry.dto.job.CreateJobDto;
import com.springboot.domesticworkregistry.entities.Job;

@Mapper(componentModel = "spring")
public interface JobMapper {
    Job toJob(CreateJobDto dto);

}
