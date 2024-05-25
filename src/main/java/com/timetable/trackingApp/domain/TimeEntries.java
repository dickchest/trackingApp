package com.timetable.trackingApp.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
public class TimeEntries {
    private String id;
    private String userId;
    private String categoryId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Duration duration;
    private LocalDateTime updateDate;
}
