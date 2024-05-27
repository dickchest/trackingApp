package com.timetable.trackingApp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Categories {
    private String id; // Уникальный идентификатор категории
    private String name; // имя категории
}
