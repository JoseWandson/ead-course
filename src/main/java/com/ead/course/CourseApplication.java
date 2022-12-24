package com.ead.course;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class CourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseApplication.class, args);
    }

    @PostConstruct
    private void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
