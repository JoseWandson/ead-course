package com.ead.course.repositories;

import com.ead.course.models.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<LessonModel, UUID> {

    @Query("select l from LessonModel l where l.module.moduleId = :moduleId")
    List<LessonModel> findAllLessonsIntoModule(UUID moduleId);

    @Query("select l from LessonModel l where l.module.moduleId = :moduleId and l.lessonId = :lessonId")
    Optional<LessonModel>  findLessonIntoModule(UUID moduleId, UUID lessonId);
}
