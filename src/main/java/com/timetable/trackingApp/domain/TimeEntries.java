package com.timetable.trackingApp.domain;

import com.google.cloud.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimeEntries {
    private String id;
    private String userId;
    private String categoryId;
    private Timestamp startDate;
    private Timestamp endDate;
    private Long duration; // duration is seconds
}
