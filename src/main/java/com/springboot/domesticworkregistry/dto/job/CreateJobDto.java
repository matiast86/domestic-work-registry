package com.springboot.domesticworkregistry.dto.job;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.springboot.domesticworkregistry.dto.job.groups.ExtraJobValidation;
import com.springboot.domesticworkregistry.dto.job.groups.RegularJobValidation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobDto {

    private Integer jobId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "is required", groups = { RegularJobValidation.class, ExtraJobValidation.class })
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "is required", groups = { RegularJobValidation.class, ExtraJobValidation.class })
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "is required", groups = { RegularJobValidation.class, ExtraJobValidation.class })
    private LocalTime endTime;

    @NotNull(message = "is required", groups = RegularJobValidation.class)
    private BigDecimal hourlyRate;

    private BigDecimal transportationFee = BigDecimal.ZERO.setScale(2);

    private boolean extraHours;
}
