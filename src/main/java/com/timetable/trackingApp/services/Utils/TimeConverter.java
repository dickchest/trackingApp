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
        return TimeEntriesDto.builder()
                .id(entity.getId())
                .categoryId(entity.getCategoryId())
                .startDate(toLocalTime(entity.getStartDate()))
                .endDate(toLocalTime(entity.getEndDate()))
                .duration(entity.getDuration())
                .build();
    }

    public static TimeEntries fromDto(TimeEntriesDto dto) {
        TimeEntries entity = null;
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
