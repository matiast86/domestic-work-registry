package com.springboot.domesticworkregistry.service.job;

import java.util.List;

import com.springboot.domesticworkregistry.dto.job.CreateJobDto;
import com.springboot.domesticworkregistry.entities.Job;

public interface JobService {

    List<Job> findAll();

    List<Job> getJobsByContract(int contractId);

    Job findById(int id);

    Job save(CreateJobDto form, int contractId);

    void delete(int id);

}
