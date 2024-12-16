package com.ead.course.controllers;

import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.UserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import com.ead.course.specifications.SpecificationTemplate;
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
@RequestMapping("/courses/{courseId}/users")
public class CourseUserController {

    private final CourseService courseService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Object> getAllUsersByCourse(SpecificationTemplate.UserSpec spec,
                                                      @PageableDefault(sort = "userId") Pageable pageable,
                                                      @PathVariable UUID courseId) {
        if (courseService.existsById(courseId)) {
            return ResponseEntity
                    .ok(userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
    }

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable UUID courseId,
                                                               @RequestBody @Valid SubscriptionDto subscriptionDto) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }

        if (courseService.existsByCourseAndUser(courseId, subscriptionDto.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");
        }

        Optional<UserModel> userModelOptional = userService.findById(subscriptionDto.getUserId());
        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        if (userModelOptional.get().getUserStatus().equals(UserStatus.BLOCKED.toString())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is blocked.");
        }

        courseService.saveSubscriptionUserInCourseAndSendNotification(courseModelOptional.get(), userModelOptional.get());

        return ResponseEntity.status(HttpStatus.CREATED).body("Subscription created successfully.");
    }
}
