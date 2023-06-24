package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
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
@RequestMapping("/modules/{moduleId}/lessons")
public class LessonController {

    private static final String LESSON_NOT_FOUND_FOR_THIS_MODULE = "Lesson not found for this module.";

    private final LessonService lessonService;
    private final ModuleService moduleService;

    @PostMapping
    public ResponseEntity<Object> saveLesson(@PathVariable UUID moduleId,
                                             @RequestBody @Valid LessonDto lessonDto) {
        log.debug("POST saveLesson lessonDto received {} ", lessonDto);
        Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);
        if (moduleModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found.");
        }

        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel);
        lessonModel.setModule(moduleModelOptional.get());

        log.debug("POST saveLesson lessonId saved {} ", lessonModel.getLessonId());
        log.info("Lesson saved successfully lessonId {} ", lessonModel.getLessonId());
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<String> deleteLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId) {
        log.debug("DELETE deleteLesson lessonId received {} ", lessonId);
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (lessonModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LESSON_NOT_FOUND_FOR_THIS_MODULE);
        }

        lessonService.delete(lessonModelOptional.get());

        log.debug("DELETE deleteLesson lessonId deleted {} ", lessonId);
        log.info("Lesson deleted successfully lessonId {} ", lessonId);
        return ResponseEntity.ok("Lesson deleted successfully.");
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId,
                                               @RequestBody @Valid LessonDto lessonDto) {
        log.debug("PUT updateLesson lessonDto received {} ", lessonDto);
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (lessonModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LESSON_NOT_FOUND_FOR_THIS_MODULE);
        }

        var lessonModel = lessonModelOptional.get();
        BeanUtils.copyProperties(lessonDto, lessonModel);

        log.debug("PUT updateLesson lessonId updated {} ", lessonId);
        log.info("Lesson updated successfully lessonId {} ", lessonId);
        return ResponseEntity.ok(lessonService.save(lessonModel));
    }

    @GetMapping
    public ResponseEntity<Page<LessonModel>> getAllLessons(@PathVariable UUID moduleId,
                                                           SpecificationTemplate.LessonSpec spec,
                                                           @PageableDefault(sort = "lessonId") Pageable pageable) {
        return ResponseEntity
                .ok(lessonService.findAllByModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<Object> getOneLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId) {
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        return lessonModelOptional.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(LESSON_NOT_FOUND_FOR_THIS_MODULE));
    }
}
