package com.springboot.domesticworkregistry.entities;

import com.springboot.domesticworkregistry.enums.JobType;

public class Employee {
    private long id;
    private String cuil;
    private JobType jobType;

    public Employee() {
    }

    public Employee(String cuil, JobType jobType) {
        this.cuil = cuil;
        this.jobType = jobType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    @Override
    public String toString() {
        return "Employee {id=" + id + ", cuil=" + cuil + ", JobType=" + jobType + "}";
    }
}
