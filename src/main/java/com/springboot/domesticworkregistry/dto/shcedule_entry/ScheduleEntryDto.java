package com.springboot.domesticworkregistry.dto.shcedule_entry;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntryDto {

    @NotBlank(message = "is required")
    private DayOfWeek dayOfWeek;

    @NotBlank(message = "is required")
    private LocalTime startTime;

    @NotBlank(message = "is required")
    private LocalTime endTime;

}
