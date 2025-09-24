package com.springboot.domesticworkregistry.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.springboot.domesticworkregistry.enums.EmploymentType;
import com.springboot.domesticworkregistry.enums.JobType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(name = "name")
        private String name;

        @Column(name = "since")
        private LocalDate since;

        @Column(name = "start_date")
        private LocalDate startDate;

        @Column(name = "end_date")
        private LocalDate endDate;

        @Column(name = "job_type")
        @Enumerated(EnumType.STRING)
        private JobType jobType;

        @Column(name = "employment_type")
        @Enumerated(EnumType.STRING)
        private EmploymentType employmentType;

        @Column(name = "salary")
        private BigDecimal salary;

        @Column(name = "active")
        private boolean active;

        @ManyToOne
        @JoinColumn(name = "employer_id")
        private User employer;

        @ManyToOne
        @JoinColumn(name = "employee_id")
        private User employee;

        @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<Job> jobs = new ArrayList<>();

        @OneToOne
        @JoinColumn(name = "schedule_id")
        private Schedule schedule;

        @OneToOne
        @JoinColumn(name = "work_address")
        private Address workAddress; // Generaly the emloyer's address

        public int getService() {
                if (since == null)
                        return 0;
                return Period.between(since, LocalDate.now()).getYears();
        }

        public void addJob(Job job) {
                jobs.add(job);
                job.setContract(this);
        }

}
