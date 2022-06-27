package com.ead.course.repositories;

import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID>, JpaSpecificationExecutor<ModuleModel> {

    @Query("select m from ModuleModel m where m.course.courseId = :courseId")
    List<ModuleModel> findAllModulesIntoCourse(UUID courseId);

    @Query("select m from ModuleModel m where m.course.courseId = :courseId and m.moduleId = :moduleId")
    Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);
}
