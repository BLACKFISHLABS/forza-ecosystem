package io.github.blackfishlabs.forza.core.application.address.repository;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.address.model.StateEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.List;

public class StateSpecification {

    private StateSpecification() {
    }

    public static Specification<StateEntity> findByName(final String name) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            if (!Strings.isNullOrEmpty(name)) {
                predicates.add(builder.like(builder.lower(root.get("state")), "%" + name.toLowerCase() + "%"));
            }

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
