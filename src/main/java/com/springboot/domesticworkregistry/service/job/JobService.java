package com.springboot.domesticworkregistry.service.job;

import java.util.List;

import com.springboot.domesticworkregistry.dto.job.CreateJobDto;
import com.springboot.domesticworkregistry.dto.job.JobsMonthlyReportDto;
import com.springboot.domesticworkregistry.dto.job.JobsReportDto;
import com.springboot.domesticworkregistry.entities.Job;

public interface JobService {

    List<Job> findAll();

    List<Job> getJobsByContract(int contractId);

    JobsReportDto getJobsByContracDto(int contractId);

    JobsMonthlyReportDto getMonthlyJobsByContract(int contractId, int year, int month);

    Job findById(int id);

    Job save(CreateJobDto form, int contractId);

    CreateJobDto getJobDto(int jobId);

    Job update(CreateJobDto form);

    void delete(int id);

}
