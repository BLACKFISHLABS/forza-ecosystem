package io.github.blackfishlabs.forza.salesman.repository;

import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.helper.FormattingUtils;
import io.github.blackfishlabs.forza.salesman.model.SalesmanEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.List;

public class SalesmanSpecification {

    private SalesmanSpecification() {
    }

    public static Specification<SalesmanEntity> findByDocumentAndPassword(final String document, final String password) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            predicates.add(builder.equal(root.get("cpfOrCnpj"), FormattingUtils.formatCpforCnpj(document)));
            predicates.add(builder.equal(root.get("password"), password));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<SalesmanEntity> findByDocument(final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            predicates.add(builder.equal(root.get("cpfOrCnpj"), FormattingUtils.formatCpforCnpj(document)));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<SalesmanEntity> findByCompany(final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            Join<SalesmanEntity, CompanyEntity> company = root.join("companies");

            predicates.add(builder.equal(company.get("cnpj"), FormattingUtils.formatCpforCnpj(document)));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

}
