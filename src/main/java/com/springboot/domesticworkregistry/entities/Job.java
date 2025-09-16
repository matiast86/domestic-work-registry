package com.springboot.domesticworkregistry.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(name = "date")
        private LocalDate date;

        @Column(name = "start_time")
        private LocalTime startTime;

        @Column(name = "end_time")
        private LocalTime endTime;

        @Column(name = "worked_hours")
        private Double workedHours;

        @Column(name = "hourly_rate")
        private BigDecimal hourlyRate;

        @Column(name = "partial_fee")
        private BigDecimal partialFee;

        @Column(name = "transportation_fee")
        private BigDecimal transportationFee = BigDecimal.ZERO;

        @Column(name = "total_fee")
        private BigDecimal totalFee;

        @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                        CascadeType.REFRESH })
        @JoinColumn(name = "contract_id")
        private Contract contract;

        public Job(LocalDate date, LocalTime startTime, LocalTime endTime, Double workedHours, BigDecimal hourlyRate,
                        BigDecimal partialFee, BigDecimal transportationFee, BigDecimal totalFee) {
                this.date = date;
                this.startTime = startTime;
                this.endTime = endTime;
                this.workedHours = workedHours;
                this.hourlyRate = hourlyRate;
                this.partialFee = partialFee;
                this.transportationFee = transportationFee;
                this.totalFee = totalFee;
        }
}
