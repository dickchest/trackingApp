package com.timetable.trackingApp.scheduler;

import com.timetable.trackingApp.services.TopUserService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduleExecutor {
    private final TopUserService topUserService;

    public ScheduleExecutor(TopUserService topUserService) {
        this.topUserService = topUserService;
    }

    @Scheduled(fixedDelayString = "P1D")
    public void fixedDelayTask() {
        System.out.println("One minute passed");
        topUserService.deleteUsersExceptTopTen();
    }
}
