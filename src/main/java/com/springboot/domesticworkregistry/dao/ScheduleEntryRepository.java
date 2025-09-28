package com.springboot.domesticworkregistry.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.ScheduleEntry;

public interface ScheduleEntryRepository extends JpaRepository<ScheduleEntry, Integer> {

}
