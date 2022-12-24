package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.filters.CourseFilter;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.CourseSpecs;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/courses")
public class CourseController {

    private static final String COURSE_NOT_FOUND = "Course Not Found.";

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseModel> saveCourse(@RequestBody @Valid CourseDto courseDto) {
        log.debug("POST saveCourse courseDto received {} ", courseDto);
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);

        log.debug("POST saveCourse courseId saved {} ", courseModel.getCourseId());
        log.info("Course saved successfully courseId {} ", courseModel.getCourseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable UUID courseId) {
        log.debug("DELETE deleteCourse courseId received {} ", courseId);
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(COURSE_NOT_FOUND);
        }

        courseService.delete(courseModelOptional.get());

        log.debug("DELETE deleteCourse courseId deleted {} ", courseId);
        log.info("Course deleted successfully courseId {} ", courseId);
        return ResponseEntity.ok("Course deleted successfully.");
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable UUID courseId, @RequestBody @Valid CourseDto courseDto) {
        log.debug("PUT updateCourse courseDto received {} ", courseDto);
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(COURSE_NOT_FOUND);
        }

        var courseModel = courseModelOptional.get();
        BeanUtils.copyProperties(courseDto, courseModel);

        log.debug("PUT updateCourse courseId updated {} ", courseId);
        log.info("Course updated successfully courseId {} ", courseId);
        return ResponseEntity.ok(courseService.save(courseModel));
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(CourseFilter filter,
                                                           @PageableDefault(sort = "courseId") Pageable pageable,
                                                           @RequestParam(required = false) UUID userId) {
        Specification<CourseModel> specification = CourseSpecs.usingFilter(filter);

        if (Objects.nonNull(userId)) {
            specification = specification.and(CourseSpecs.courseUserId(userId));
        }

        return ResponseEntity.ok(courseService.findAll(specification, pageable));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        return courseModelOptional.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(COURSE_NOT_FOUND));
    }
}
