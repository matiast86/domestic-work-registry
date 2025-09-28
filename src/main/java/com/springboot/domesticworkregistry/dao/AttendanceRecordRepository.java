package com.springboot.domesticworkregistry.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.AttendanceRecord;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Integer> {

}
