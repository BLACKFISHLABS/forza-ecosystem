package io.github.blackfishlabs.forza.charge.repository;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.charge.model.ChargeEntity;
import io.github.blackfishlabs.forza.charge.model.ChargeStatus;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.helper.FormattingUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.List;

public class ChargeSpecification {

    private static final String OWNER = "owner";

    private ChargeSpecification() {
    }

    public static Specification<ChargeEntity> findByCompany(final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            Join<ChargeEntity, CompanyEntity> company = root.join(OWNER);

            predicates.add(builder.equal(company.get("cnpj"), document));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<ChargeEntity> findByName(final String search, final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicatesOr = Lists.newArrayList();

            if (!Strings.isNullOrEmpty(search)) {
                predicatesOr.add(builder.like(builder.lower(root.get("code")), "%" + search.toLowerCase() + "%"));
            }

            List<Predicate> predicatesAnd = Lists.newArrayList();

            Join<ChargeEntity, CompanyEntity> company = root.join(OWNER);
            predicatesAnd.add(builder.equal(company.get("cnpj"), document));

            return builder.and(builder.or(predicatesOr.toArray(new Predicate[]{})), builder.and(predicatesAnd.toArray(new Predicate[]{})));
        };
    }

    public static Specification<ChargeEntity> findByCodeAndSalesman(final String code, final String document, final String salesman) {

        return (root, query, builder) -> {
            List<Predicate> predicatesOr = Lists.newArrayList();

            if (!Strings.isNullOrEmpty(code)) {
                predicatesOr.add(builder.equal(builder.lower(root.get("code")), code.toLowerCase()));
            }

            List<Predicate> predicatesAnd = Lists.newArrayList();

            Join<ChargeEntity, CompanyEntity> company = root.join(OWNER);
            predicatesAnd.add(builder.equal(company.get("cnpj"), FormattingUtils.formatCpforCnpj(document)));
            predicatesAnd.add(builder.equal(root.get("salesmanCode"), FormattingUtils.formatCpforCnpj(salesman)));

            return builder.and(builder.or(predicatesOr.toArray(new Predicate[]{})), builder.and(predicatesAnd.toArray(new Predicate[]{})));
        };
    }

    public static Specification<ChargeEntity> findByCodeAndCompany(final String code, final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicatesOr = Lists.newArrayList();

            if (!Strings.isNullOrEmpty(code)) {
                predicatesOr.add(builder.equal(builder.lower(root.get("code")), code.toLowerCase()));
            }

            List<Predicate> predicatesAnd = Lists.newArrayList();

            Join<ChargeEntity, CompanyEntity> company = root.join(OWNER);
            predicatesAnd.add(builder.equal(company.get("cnpj"), FormattingUtils.formatCpforCnpj(document)));

            return builder.and(builder.or(predicatesOr.toArray(new Predicate[]{})), builder.and(predicatesAnd.toArray(new Predicate[]{})));
        };
    }

    public static Specification<ChargeEntity> findByStatus(final ChargeStatus status) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();
            predicates.add(builder.equal(root.get("status"), status));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
