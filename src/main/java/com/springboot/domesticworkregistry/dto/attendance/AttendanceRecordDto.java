package com.springboot.domesticworkregistry.dto.attendance;

import java.time.LocalDate;

import com.springboot.domesticworkregistry.enums.AttendanceStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordDto {
    private int attendanceId;
    private LocalDate date;
    private AttendanceStatus attendanceStatus;
}
