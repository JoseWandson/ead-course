package com.ead.course.repositories;

import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {

    @Query("select m from ModuleModel m where m.course.courseId = :courseId")
    List<ModuleModel> findAllModulesIntoCourse(UUID courseId);
}
