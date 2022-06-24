package com.ead.course.specifications;

import com.ead.course.filters.CourseFilter;
import com.ead.course.models.CourseModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseSpecs {

    public static Specification<CourseModel> usandoFiltro(CourseFilter filter) {
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
}
