package com.springboot.domesticworkregistry.service.job;

import java.util.List;

import com.springboot.domesticworkregistry.entities.Job;

public interface JobService {

    List<Job> findAll();

    List<Job> getJobsByContract(String contractId);

    Job findById(String id);

    Job save(Job job, String contractId);

    void delete(String id);

}
