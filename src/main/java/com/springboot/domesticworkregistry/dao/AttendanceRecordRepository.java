package com.springboot.domesticworkregistry.dao;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.domesticworkregistry.entities.AttendanceRecord;
import com.springboot.domesticworkregistry.entities.ScheduleEntry;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Integer> {
    boolean existsByScheduleEntryAndDate(ScheduleEntry entry, LocalDate date);
}
