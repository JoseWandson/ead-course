package com.ead.course.validation;

import com.ead.course.dtos.CourseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CourseValidator implements Validator {

    private final Validator validator;

    public CourseValidator(@Qualifier("mvcValidator") Validator validator) {
        this.validator = validator;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        var courseDto = (CourseDto) target;
        validator.validate(courseDto, errors);
    }
}
