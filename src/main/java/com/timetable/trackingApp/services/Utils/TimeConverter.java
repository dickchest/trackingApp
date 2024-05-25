package com.timetable.trackingApp.services.Utils;

import com.google.cloud.Timestamp;
import com.timetable.trackingApp.domain.TimeEntries;
import com.timetable.trackingApp.dto.TimeEntriesDto;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TimeConverter {
    public TimeEntriesDto toDto(TimeEntries entity) {
        return TimeEntriesDto.builder()
                .id(entity.getId())
                .categoryId(entity.getCategoryId())
                .startDate(toLocalTime(entity.getStartDate()))
                .endDate(toLocalTime(entity.getEndDate()))
                .duration(entity.getDuration())
                .build();
    }

    private LocalDateTime toLocalTime(Timestamp timestamp) {

        return timestamp.toDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private Timestamp toTimeStamp(LocalDateTime localDateTime) {
        Timestamp timestamp = Timestamp.of(Date.from(localDateTime));
        return timestamp;
    }
}
