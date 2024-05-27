package com.timetable.trackingApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeEntriesDto {
    private String id;
    private String userId;
    private String categoryId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long duration; // duration is seconds
}
