package com.ead.course.controllers;

import com.ead.course.clients.CourseClient;
import com.ead.course.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/courses/{courseId}/users")
public class CourseUserController {

    @Autowired
    private CourseClient courseClient;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsersByCourse(@PageableDefault(sort = "userId") Pageable pageable,
                                                             @PathVariable UUID courseId) {
        return ResponseEntity.ok(courseClient.getAllUsersByCourse(courseId, pageable));
    }
}
