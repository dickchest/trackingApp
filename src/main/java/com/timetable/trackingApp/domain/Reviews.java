package com.timetable.trackingApp.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reviews {
    private String id; // Уникальный идентификатор отзыва.
    private String fromUserId; // ID пользователя, который оставил отзыв.
    private String toUserId; // ID пользователя, которому оставлен отзыв.
    private int rating; // Рейтинг.
    private String comment; // Комментарий
}
