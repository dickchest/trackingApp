package com.timetable.trackingApp.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetails {
    private String id;
    private String email;
    private String password;
    private String displayName;
}
