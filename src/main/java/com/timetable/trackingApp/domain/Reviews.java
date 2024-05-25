package com.timetable.trackingApp.domain;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Reviews {
    @Setter
    private String id; // Уникальный идентификатор отзыва.
    @Setter
    private String fromUserId; // ID пользователя, который оставил отзыв.
    private String toUserId; // ID пользователя, которому оставлен отзыв.
    @Setter
    private Integer rating; // Рейтинг.
    @Setter
    private String comment; // Комментарий

    public void setToUserId(String toUserId) {
        // может быть только установлено на существующего пользователя
        try {
            FirebaseAuth.getInstance().getUser(toUserId);
            this.toUserId = toUserId;
        } catch (FirebaseAuthException e) {
            throw new RuntimeException("ToUser not found");
        }
    }
}
