package com.springboot.domesticworkregistry.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

}
