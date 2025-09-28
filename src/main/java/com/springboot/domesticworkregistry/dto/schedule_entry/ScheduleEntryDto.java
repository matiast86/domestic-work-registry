package com.springboot.domesticworkregistry.dto.schedule_entry;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntryDto {

    @NotNull(message = "is required")
    private DayOfWeek dayOfWeek;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "is required")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "is required")
    private LocalTime endTime;

}
