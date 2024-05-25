package com.timetable.trackingApp.domain;

import com.google.cloud.Timestamp;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class TimeEntries {
    private String id;
    private String userId;
    private String categoryId;
    private String startDate;
    private String endDate;
    private Long duration; // duration is seconds
//    private Timestamp updateDate;
}
