package com.ead.course.specifications;

import com.ead.course.filters.CourseFilter;
import com.ead.course.filters.LessonFilter;
import com.ead.course.filters.ModuleFilter;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseSpecs {

    public static Specification<CourseModel> usingFilter(CourseFilter filter) {
        return (root, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            if (Objects.nonNull(filter.getCourseLevel())) {
                predicates.add(builder.equal(root.get("courseLevel"), filter.getCourseLevel()));
            }
            if (Objects.nonNull(filter.getName())) {
                predicates.add(builder.like(root.get("name"), "%" + filter.getName() + "%"));
            }
            if (Objects.nonNull(filter.getCourseStatus())) {
                predicates.add(builder.equal(root.get("courseStatus"), filter.getCourseStatus()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ModuleModel> usingFilter(ModuleFilter filter) {
        return (root, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            if (Objects.nonNull(filter.getTitle())) {
                predicates.add(builder.like(root.get("title"), "%" + filter.getTitle() + "%"));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<CourseModel> course = query.from(CourseModel.class);
            Expression<Set<ModuleModel>> modules = course.get("modules");
            return cb.and(cb.equal(course.get("courseId"), courseId), cb.isMember(root, modules));
        };
    }

    public static Specification<LessonModel> usingFilter(LessonFilter filter) {
        return (root, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            if (Objects.nonNull(filter.getTitle())) {
                predicates.add(builder.like(root.get("title"), "%" + filter.getTitle() + "%"));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<LessonModel> lessonModuleId(final UUID moduleId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<ModuleModel> module = query.from(ModuleModel.class);
            Expression<Set<LessonModel>> lessons = module.get("lessons");
            return cb.and(cb.equal(module.get("moduleId"), moduleId), cb.isMember(root, lessons));
        };
    }
}
