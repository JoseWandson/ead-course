package com.ead.course.filters;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseFilter {

    private CourseLevel courseLevel;
    private CourseStatus courseStatus;
    private String name;

}
