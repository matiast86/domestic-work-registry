package com.springboot.domesticworkregistry.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.domesticworkregistry.entities.AttendanceRecord;
import com.springboot.domesticworkregistry.entities.ScheduleEntry;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Integer> {
        boolean existsByScheduleEntryAndDate(ScheduleEntry entry, LocalDate date);

        @Query("SELECT a FROM AttendanceRecord a " +
                        "WHERE a.scheduleEntry.schedule.id = :scheduleId " +
                        "AND FUNCTION('YEAR', a.date) = :year " +
                        "AND FUNCTION('MONTH', a.date) = :month ORDER BY a.date ASC")
        List<AttendanceRecord> findByScheduleAndMonth(@Param("scheduleId") int scheduleId,
                        @Param("year") int year,
                        @Param("month") int month);

}
