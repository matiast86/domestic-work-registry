package com.springboot.domesticworkregistry.dto.job;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobDto {

    @NotNull(message = "is requied")
    private LocalDate date;

    @NotNull(message = "is requied")
    private LocalTime starTime;

    @NotNull(message = "is requied")
    private LocalTime endTime;

    @NotNull(message = "is requied")
    private Double hourlyRate;

    @NotNull(message = "is requied")
    private Double transportationFee;
}
