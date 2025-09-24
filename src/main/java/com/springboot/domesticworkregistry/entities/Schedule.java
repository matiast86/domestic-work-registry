package com.springboot.domesticworkregistry.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @OneToOne
    private Contract contract;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleEntry> entries = new ArrayList<>();

    public void addEntry(ScheduleEntry entry) {
        entries.add(entry);
        entry.setSchedule(this);
    }

}
