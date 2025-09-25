package com.springboot.domesticworkregistry.service.schedule;

import java.util.List;

import com.springboot.domesticworkregistry.dao.ScheduleRepository;
import com.springboot.domesticworkregistry.entities.Schedule;
import com.springboot.domesticworkregistry.exceptions.EntityNotFoundException;

public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule findById(int id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule with id " + id + " not found"));
    }

    @Override
    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

}
