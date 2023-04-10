package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CourseUserRepository extends JpaRepository<CourseUserModel, UUID> {

    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);

    boolean existsByUserId(UUID userId);

    @Query("select c from CourseUserModel c where c.course.courseId = :courseId")
    List<CourseUserModel> findAllCourseUserIntoCourse(UUID courseId);

    void deleteAllByUserId(UUID userId);
}
