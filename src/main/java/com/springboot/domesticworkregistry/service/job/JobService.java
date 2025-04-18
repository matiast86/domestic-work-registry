package com.springboot.domesticworkregistry.service.job;

import java.util.List;

import com.springboot.domesticworkregistry.entities.Job;

public interface JobService {

    List<Job> findAll();

    List<Job> getJobsByEmployee(String employerId, String employeeId);

    Job findById(String id);

    Job save(Job job, String employerId);

    void delete(String id);

}
