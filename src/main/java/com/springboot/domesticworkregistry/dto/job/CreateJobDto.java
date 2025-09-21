package com.springboot.domesticworkregistry.dto.job;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "is required")
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "is required")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "is required")
    private LocalTime endTime;

    @NotNull(message = "is required")
    private BigDecimal hourlyRate;

    @NotNull(message = "is required")
    private BigDecimal transportationFee;
}
