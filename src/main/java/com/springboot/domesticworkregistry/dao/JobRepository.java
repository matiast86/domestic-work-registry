package com.springboot.domesticworkregistry.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.Job;

public interface JobRepository extends JpaRepository<Job, Integer> {

    Optional<Job> findById(int id);

}
