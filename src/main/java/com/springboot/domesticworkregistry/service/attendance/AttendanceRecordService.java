package com.springboot.domesticworkregistry.service.attendance;

import java.time.LocalDate;
import java.util.List;

import com.springboot.domesticworkregistry.entities.AttendanceRecord;
import com.springboot.domesticworkregistry.enums.AttendanceStatus;

public interface AttendanceRecordService {

    public int generateMonthlyAttendance(int scheduleId, int year, int month);

    public void generateSingleAttendance(int contractId, LocalDate date, AttendanceStatus status);

    public List<AttendanceRecord> findByScheduleAndMonth(int scheduleId, int year, int month);

    public void updateStatus(int recordId, AttendanceStatus newStatus);

    public int getContractId(int scheduleId);



}
