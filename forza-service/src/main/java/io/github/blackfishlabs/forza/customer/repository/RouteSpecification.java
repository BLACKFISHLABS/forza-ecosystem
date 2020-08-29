package io.github.blackfishlabs.forza.customer.repository;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.customer.model.RouteEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.List;

public class RouteSpecification {

    private static final String OWNER = "owner";

    private RouteSpecification() {
    }

    public static Specification<RouteEntity> findByCompany(final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            Join<RouteEntity, CompanyEntity> company = root.join(OWNER);

            predicates.add(builder.equal(company.get("cnpj"), document));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<RouteEntity> findByParamAndCompany(final String search, final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicatesOr = Lists.newArrayList();

            if (!Strings.isNullOrEmpty(search)) {
                predicatesOr.add(builder.like(builder.lower(root.get("code")), "%" + search.toLowerCase() + "%"));
                predicatesOr.add(builder.like(builder.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
            }

            List<Predicate> predicatesAnd = Lists.newArrayList();

            Join<RouteEntity, CompanyEntity> company = root.join(OWNER);
            predicatesAnd.add(builder.equal(company.get("cnpj"), document));

            return builder.and(builder.or(predicatesOr.toArray(new Predicate[]{})), builder.and(predicatesAnd.toArray(new Predicate[]{})));
        };
    }
}
