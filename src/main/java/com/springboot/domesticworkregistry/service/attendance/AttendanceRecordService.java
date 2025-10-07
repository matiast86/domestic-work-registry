package com.springboot.domesticworkregistry.service.attendance;

import java.time.LocalDate;

import com.springboot.domesticworkregistry.enums.AttendanceStatus;

public interface AttendanceRecordService {

    public int generateMonthlyAttendance(int scheduleId, int year, int month);

    public void generateSingleAttendance(int contractId, LocalDate date, AttendanceStatus status);

}
