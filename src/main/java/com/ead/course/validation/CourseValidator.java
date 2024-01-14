package com.ead.course.validation;

import com.ead.course.dtos.CourseDto;
import com.ead.course.enums.UserType;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    private final Validator validator;
    private final UserService userService;

    public CourseValidator(@Qualifier("mvcValidator") Validator validator, UserService userService) {
        this.validator = validator;
        this.userService = userService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        var courseDto = (CourseDto) target;
        validator.validate(courseDto, errors);

        if (!errors.hasErrors()) {
            validateUserInstructor(courseDto.getUserInstructor(), errors);
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors errors) {
        Optional<UserModel> userModelOptional = userService.findById(userInstructor);
        if (userModelOptional.isEmpty()) {
            errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found.");
            return;
        }

        if (userModelOptional.get().getUserType().equals(UserType.STUDENT.toString())) {
            errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR ou ADMIN.");
        }
    }
}
