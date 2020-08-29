package io.github.blackfishlabs.forza.order.repository;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.order.model.OrderEntity;
import io.github.blackfishlabs.forza.order.model.OrderStatus;
import io.github.blackfishlabs.forza.salesman.model.SalesmanEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderSpecification {

    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String OWNER = "owner";

    public static Specification<OrderEntity> findByCompanyAndSalesmanAndLastUpdate(final String document, final String cpfCnpj, final String lastUpdateTime) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            Join<OrderEntity, CompanyEntity> company = root.join(OWNER);
            Join<OrderEntity, SalesmanEntity> salesman = root.join("salesman");

            predicates.add(builder.equal(company.get("cnpj"), document));
            predicates.add(builder.equal(salesman.get("cpfOrCnpj"), cpfCnpj));

            Date parse;
            try {
                parse = new SimpleDateFormat(FORMAT).parse(lastUpdateTime);
                predicates.add(builder.greaterThan(root.get("lastChangeTime"), parse));
            } catch (ParseException e) {
                // Not converted
            }

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<OrderEntity> findByCompany(final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            Join<OrderEntity, CompanyEntity> company = root.join(OWNER);

            predicates.add(builder.equal(company.get("cnpj"), document));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<OrderEntity> findByDescription(final String search, final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicatesOr = Lists.newArrayList();

            if (!Strings.isNullOrEmpty(search)) {
                predicatesOr.add(builder.like(builder.lower(root.get("resumeCode")), "%" + search.toLowerCase() + "%"));
            }

            List<Predicate> predicatesAnd = Lists.newArrayList();

            Join<OrderEntity, CompanyEntity> company = root.join(OWNER);
            predicatesAnd.add(builder.equal(company.get("cnpj"), document));

            return builder.and(
                    builder.or(predicatesOr.toArray(new Predicate[]{})),
                    builder.and(predicatesAnd.toArray(new Predicate[]{})));
        };
    }

    public static Specification<OrderEntity> findByStatus(final OrderStatus status) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();
            predicates.add(builder.equal(root.get("status"), status));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
