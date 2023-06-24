package com.ead.course.controllers;

import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses/")
public class CourseUserController {

    private final CourseService courseService;

    @GetMapping("{courseId}/users")
    public ResponseEntity<Object> getAllUsersByCourse(@PageableDefault(sort = "userId") Pageable pageable,
                                                      @PathVariable UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }

        return ResponseEntity.ok("");
    }

    @PostMapping("{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable UUID courseId,
                                                               @RequestBody @Valid SubscriptionDto subscriptionDto) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
}
