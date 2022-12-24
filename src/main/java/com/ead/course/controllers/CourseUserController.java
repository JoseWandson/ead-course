package com.ead.course.controllers;

import com.ead.course.clients.CourseClient;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/courses/{courseId}/users")
public class CourseUserController {

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseUserService courseUserService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsersByCourse(@PageableDefault(sort = "userId") Pageable pageable,
                                                             @PathVariable UUID courseId) {
        return ResponseEntity.ok(courseClient.getAllUsersByCourse(courseId, pageable));
    }

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable UUID courseId,
                                                               @RequestBody @Valid SubscriptionDto subscriptionDto) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }

        CourseModel courseModel = courseModelOptional.get();
        UUID userId = subscriptionDto.getUserId();
        if (courseUserService.existsByCourseAndUserId(courseModel, userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription alredy exists!");
        }

        courseUserService.save(courseModel.convertToCourseUserModel(userId));

        return ResponseEntity.status(HttpStatus.CREATED).body("Subscription created successfully.");
    }
}
