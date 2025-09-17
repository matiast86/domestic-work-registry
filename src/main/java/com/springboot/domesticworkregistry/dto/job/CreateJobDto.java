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

    @DateTimeFormat(pattern = "dd-MM-yyy")
    @NotNull(message = "is requied")
    private LocalDate jobDate;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "is requied")
    private LocalTime starTime;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "is requied")
    private LocalTime endTime;

    @NotNull(message = "is requied")
    private BigDecimal hourlyRate;

    @NotNull(message = "is requied")
    private BigDecimal transportationFee;
}
