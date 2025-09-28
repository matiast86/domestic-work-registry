package com.springboot.domesticworkregistry.dto.contract;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.springboot.domesticworkregistry.entities.Contract;
import com.springboot.domesticworkregistry.entities.Schedule;
import com.springboot.domesticworkregistry.entities.ScheduleEntry;
import com.springboot.domesticworkregistry.entities.User;

@Component
public class ContractMapper {

    public Contract fromForm(CreateEmployeeFormDto form, User employer, User employee) {
        Contract contract = new Contract();
        contract.setName(employer.getLastName().toUpperCase() + "-" + form.getLastName().toUpperCase());
        contract.setStartDate(LocalDate.now());
        contract.setActive(true);
        contract.setSince(form.getSince());
        contract.setSalary(form.getSalary());
        contract.setJobType(form.getJobType());
        contract.setEmploymentType(form.getEmploymentType());
        contract.setEmployer(employer);
        employer.addEmployerContract(contract);
        contract.setEmployee(employee);
        employee.addEmployeeContract(contract);

        // ✅ build schedule
        Schedule schedule = new Schedule();
        if (form.getEntries() != null) {
            form.getEntries().stream()
                    .filter(e -> e.getDayOfWeek() != null && e.getStartTime() != null && e.getEndTime() != null)
                    .forEach(dto -> {
                        ScheduleEntry entry = new ScheduleEntry();
                        entry.setDayOfWeek(dto.getDayOfWeek());
                        entry.setStartTime(dto.getStartTime());
                        entry.setEndTime(dto.getEndTime());
                        schedule.addEntry(entry);
                    });
        }
        contract.setSchedule(schedule);

        return contract;
    }

}