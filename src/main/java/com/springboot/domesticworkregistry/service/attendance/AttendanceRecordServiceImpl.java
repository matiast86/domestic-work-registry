package com.springboot.domesticworkregistry.service.attendance;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.domesticworkregistry.dao.AttendanceRecordRepository;
import com.springboot.domesticworkregistry.entities.AttendanceRecord;
import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Schedule;
import com.springboot.domesticworkregistry.entities.ScheduleEntry;
import com.springboot.domesticworkregistry.enums.AttendanceStatus;
import com.springboot.domesticworkregistry.exceptions.EntityNotFoundException;
import com.springboot.domesticworkregistry.service.contract.ContractService;
import com.springboot.domesticworkregistry.service.schedule.ScheduleService;

@Service
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

    private final AttendanceRecordRepository attendanceRepository;
    private final ScheduleService scheduleService;
    private final ContractService contractService;

    public AttendanceRecordServiceImpl(AttendanceRecordRepository attendanceRepository,
            ScheduleService scheduleService, ContractService contractService) {
        this.attendanceRepository = attendanceRepository;
        this.scheduleService = scheduleService;
        this.contractService = contractService;
    }

    @Override
    public int generateMonthlyAttendance(int scheduleId, int year, int month) {
        Schedule schedule = scheduleService.findById(scheduleId);

        YearMonth ym = YearMonth.of(year, month);

        List<AttendanceRecord> recordsToSave = new ArrayList<>();

        for (ScheduleEntry entry : schedule.getEntries()) {
            for (int day = 1; day <= ym.lengthOfMonth(); day++) {
                LocalDate date = ym.atDay(day);

                if (date.getDayOfWeek().equals(entry.getDayOfWeek())) {
                    boolean exists = attendanceRepository.existsByScheduleEntryAndDate(entry, date);
                    if (!exists) {
                        recordsToSave.add(new AttendanceRecord(0, date, AttendanceStatus.PRESENT, entry));
                    }
                }
            }
        }
        attendanceRepository.saveAll(recordsToSave);
        return recordsToSave.size();
    }

    @Override
    public void generateSingleAttendance(int contractId, LocalDate date, AttendanceStatus status) {
        Contract contract = contractService.findByIdWithSchedule(contractId);
        Schedule schedule = contract.getSchedule();

        ScheduleEntry entry = schedule.getEntries().stream()
                .filter(e -> e.getDayOfWeek() == date.getDayOfWeek())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Entry not found"));

        if (attendanceRepository.existsByScheduleEntryAndDate(entry, date)) {
            throw new IllegalStateException("Attendance already recorded for that date");
        }

        AttendanceRecord record = new AttendanceRecord(0, date, status, entry);
        attendanceRepository.save(record);
    }

    @Override
    public List<AttendanceRecord> findByScheduleAndMonth(int scheduleId, int year, int month) {
        return attendanceRepository.findByScheduleAndMonth(scheduleId, year, month);

    }

    @Override
    public void updateStatus(int recordId, AttendanceStatus newStatus) {
        AttendanceRecord record = attendanceRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Record with id " + recordId + " not found."));

        if (record.getAttendanceStatus() != newStatus) {
            record.setAttendanceStatus(newStatus);
            attendanceRepository.save(record);
        }
    }

    @Override
    public int getContractId(int scheduleId) {
        Schedule schedule = scheduleService.findById(scheduleId);
        return schedule.getContract().getId();
    }

}
