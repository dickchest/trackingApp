package com.timetable.trackingApp.services.Utils;

import com.google.cloud.Timestamp;
import com.timetable.trackingApp.domain.TimeEntries;
import com.timetable.trackingApp.dto.TimeEntriesDto;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class TimeConverter {
    public static TimeEntriesDto toDto(TimeEntries entity) {
        TimeEntriesDto dto = new TimeEntriesDto();
        Optional.of(entity.getId()).ifPresent(dto::setId);
        Optional.ofNullable(entity.getUserId()).ifPresent(dto::setUserId);
        Optional.ofNullable(entity.getCategoryId()).ifPresent(dto::setCategoryId);
        Optional.ofNullable(entity.getStartDate()).ifPresent(x -> dto.setStartDate(toLocalTime(entity.getStartDate())));
        Optional.ofNullable(entity.getEndDate()).ifPresent(x -> dto.setEndDate(toLocalTime(entity.getEndDate())));
        Optional.ofNullable(entity.getDuration()).ifPresent(dto::setDuration);

        return dto;
    }

    public static TimeEntries fromDto(TimeEntriesDto dto) {
        TimeEntries entity = new TimeEntries();
        Optional.of(dto.getId()).ifPresent(entity::setId);
        Optional.ofNullable(dto.getUserId()).ifPresent(entity::setUserId);
        Optional.ofNullable(dto.getCategoryId()).ifPresent(entity::setCategoryId);
        Optional.ofNullable(dto.getStartDate()).ifPresent(x -> entity.setStartDate(toTimeStamp(dto.getStartDate())));
        Optional.ofNullable(dto.getEndDate()).ifPresent(x -> entity.setEndDate(toTimeStamp(dto.getEndDate())));
        Optional.ofNullable(dto.getDuration()).ifPresent(entity::setDuration);
        return entity;
    }

    private static LocalDateTime toLocalTime(Timestamp timestamp) {

        return timestamp.toDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private static Timestamp toTimeStamp(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Timestamp.of(Date.from(instant));
    }
}
