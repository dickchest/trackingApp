package com.timetable.trackingApp.scheduler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduleExecutor {

    @Scheduled(fixedDelayString = "PT1M")
    public void fixedDelayTask() {
        System.out.println("One minute passed");
    }
}
