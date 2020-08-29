package io.github.blackfishlabs.forza.salesman.repository;

import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.salesman.model.SalesmanEntity;
import io.github.blackfishlabs.forza.salesman.model.VehicleEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.List;

public class VehicleSpecification {

    private VehicleSpecification() {
    }

    public static Specification<VehicleEntity> findByCompany(final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            Join<VehicleEntity, CompanyEntity> salesman = root.join("salesman");
            Join<SalesmanEntity, CompanyEntity> company = salesman.join("companies");

            predicates.add(builder.equal(company.get("cnpj"), document));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<VehicleEntity> findByPlate(final String plate) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            String replace = plate.replace("-", "");
            predicates.add(builder.equal(root.get("plate"), replace));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
