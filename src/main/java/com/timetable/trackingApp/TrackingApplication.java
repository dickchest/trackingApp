package com.timetable.trackingApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class TrackingApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TrackingApplication.class, args);
    }
}
