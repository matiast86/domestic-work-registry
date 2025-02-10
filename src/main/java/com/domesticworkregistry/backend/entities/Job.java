package com.domesticworkregistry.backend.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

@Component
public class Job {
    private int id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double hoursWorked;
    private Double hourlyRate;
    private Double partialFee;
    private Double transportationFee;
    private Double totalFee;
    private User user;
    private Employee employee;

    public Job() {
    }

    public Job(LocalDate date, LocalTime startTime, LocalTime endTime, Double hoursWorked, Double hourlyRate,
            Double partialFee, Double transportationFee, Double totalFee, User user, Employee employee) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hoursWorked = hoursWorked;
        this.hourlyRate = hourlyRate;
        this.partialFee = partialFee;
        this.transportationFee = transportationFee;
        this.totalFee = totalFee;
        this.user = user;
        this.employee = employee;
        calculateHoursWorked();
        calculatePartialFee();
        calculateTotalFee();
    }

    private void calculateHoursWorked() {
        long minutes = ChronoUnit.MINUTES.between(startTime, endTime);
        double fractionalHours = minutes / 60.0; // Convert minutes to hours

        // Round up to the nearest half hour
        this.hoursWorked = Math.ceil(fractionalHours * 2) / 2;
    }

    private void calculatePartialFee() {
        this.partialFee = this.hoursWorked * this.hourlyRate;
    }

    private void calculateTotalFee() {
        this.totalFee = this.partialFee + this.transportationFee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(Double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Double getPartialFee() {
        return partialFee;
    }

    public void setPartialFee(Double partialFee) {
        this.partialFee = partialFee;
    }

    public Double getTransportationFee() {
        return transportationFee;
    }

    public void setTransportationFee(Double transportationFee) {
        this.transportationFee = transportationFee;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Job [id=" + id + ", date=" + date + ", startTime=" + startTime + ", endTime=" + endTime
                + ", hoursWorked=" + hoursWorked + ", hourlyRate=" + hourlyRate + ", partialFee=" + partialFee
                + ", transportationFee=" + transportationFee + ", totalFee=" + totalFee + ", user=" + user
                + ", employee=" + employee + "]";
    }

}
