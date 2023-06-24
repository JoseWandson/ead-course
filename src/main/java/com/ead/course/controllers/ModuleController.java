package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/courses/{courseId}/modules")
public class ModuleController {

    private static final String MODULE_NOT_FOUND_FOR_THIS_COURSE = "Module not found for this course.";

    private final ModuleService moduleService;
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveModule(@PathVariable UUID courseId,
                                             @RequestBody @Valid ModuleDto moduleDto) {
        log.debug("POST saveModule moduleDto received {} ", moduleDto);
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }

        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDto, moduleModel);
        moduleModel.setCourse(courseModelOptional.get());

        log.debug("POST saveModule moduleId saved {} ", moduleModel.getModuleId());
        log.info("Module saved successfully moduleId {} ", moduleModel.getModuleId());
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<String> deleteModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
        log.debug("DELETE deleteModule moduleId received {} ", moduleId);
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (moduleModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MODULE_NOT_FOUND_FOR_THIS_COURSE);
        }

        moduleService.delete(moduleModelOptional.get());

        log.debug("DELETE deleteModule moduleId deleted {} ", moduleId);
        log.info("Module deleted successfully moduleId {} ", moduleId);
        return ResponseEntity.ok("Module deleted successfully.");
    }

    @PutMapping("/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable UUID courseId, @PathVariable UUID moduleId,
                                               @RequestBody @Valid ModuleDto moduleDto) {
        log.debug("PUT updateModule moduleDto received {} ", moduleDto);
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (moduleModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MODULE_NOT_FOUND_FOR_THIS_COURSE);
        }

        var moduleModel = moduleModelOptional.get();
        BeanUtils.copyProperties(moduleDto, moduleModel);

        log.debug("PUT updateModule moduleId updated {} ", moduleId);
        log.info("Module updated successfully moduleId {} ", moduleId);
        return ResponseEntity.ok(moduleService.save(moduleModel));
    }

    @GetMapping
    public ResponseEntity<Page<ModuleModel>> getAllModules(@PathVariable UUID courseId,
                                                           SpecificationTemplate.ModuleSpec spec,
                                                           @PageableDefault(sort = "moduleId") Pageable pageable) {
        return ResponseEntity
                .ok(moduleService.findAllByCourse(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable));
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);

        return moduleModelOptional.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(MODULE_NOT_FOUND_FOR_THIS_COURSE));
    }
}
