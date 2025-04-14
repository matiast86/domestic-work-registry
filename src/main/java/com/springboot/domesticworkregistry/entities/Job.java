package com.springboot.domesticworkregistry.entities;

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
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id;

        @Column(name = "date")
        private LocalDate date;

        @Column(name = "start_time")
        private LocalTime startTime;

        @Column(name = "end_time")
        private LocalTime endTime;

        @Column(name = "worked_hours")
        private Double workedHours;

        @Column(name = "hourly_rate")
        private Double hourlyRate;

        @Column(name = "partial_fee")
        private Double partialFee;

        @Column(name = "transportation_fee")
        private Double transportationFee = 0.0;

        @Column(name = "total_fee")
        private Double totalFee;

        @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                        CascadeType.REFRESH })
        @JoinColumn(name = "employer_id")
        private Employer employer;

        @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                        CascadeType.REFRESH })
        @JoinColumn(name = "employee_id")
        private Employee employee;

        public Job(LocalDate date, LocalTime startTime, LocalTime endTime, Double workedHours, Double hourlyRate,
                        Double partialFee, Double transportationFee, Double totalFee) {
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
