package com.springboot.domesticworkregistry.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.Job;

public interface JobRepository extends JpaRepository<Job, String> {

}
