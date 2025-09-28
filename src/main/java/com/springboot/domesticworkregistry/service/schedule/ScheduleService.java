package com.springboot.domesticworkregistry.service.schedule;

import java.util.List;

import com.springboot.domesticworkregistry.entities.Schedule;

public interface ScheduleService {

    List<Schedule> findAll();

    Schedule findById(int id);

    Schedule save(Schedule schedule);

}
