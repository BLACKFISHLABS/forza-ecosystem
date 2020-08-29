package io.github.blackfishlabs.forza.product.repository;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.product.model.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProductSpecification {

    private static final String OWNER = "owner";

    private ProductSpecification() {
    }

    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static Specification<ProductEntity> findByCompanyAndLastUpdate(final String document, final String lastUpdateTime) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            Join<ProductEntity, CompanyEntity> company = root.join(OWNER);

            predicates.add(builder.equal(company.get("cnpj"), document));

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

    public static Specification<ProductEntity> findByCompany(final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            Join<ProductEntity, CompanyEntity> company = root.join(OWNER);

            predicates.add(builder.equal(company.get("cnpj"), document));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<ProductEntity> findByParamAndCompany(final String search, final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicatesOr = Lists.newArrayList();

            if (!Strings.isNullOrEmpty(search)) {
                predicatesOr.add(builder.like(builder.lower(root.get("code")), "%" + search.toLowerCase() + "%"));
                predicatesOr.add(builder.like(builder.lower(root.get("barCode")), "%" + search.toLowerCase() + "%"));
                predicatesOr.add(builder.like(builder.lower(root.get("description")), "%" + search.toLowerCase() + "%"));
                predicatesOr.add(builder.like(builder.lower(root.get("group")), "%" + search.toLowerCase() + "%"));
            }

            List<Predicate> predicatesAnd = Lists.newArrayList();

            Join<ProductEntity, CompanyEntity> company = root.join(OWNER);
            predicatesAnd.add(builder.equal(company.get("cnpj"), document));

            return builder.and(builder.or(predicatesOr.toArray(new Predicate[]{})), builder.and(predicatesAnd.toArray(new Predicate[]{})));
        };
    }

    public static Specification<ProductEntity> findByCodeAndCompany(final String search, final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicatesOr = Lists.newArrayList();

            if (!Strings.isNullOrEmpty(search)) {
                predicatesOr.add(builder.equal(root.get("code"), search.toLowerCase()));
            }

            List<Predicate> predicatesAnd = Lists.newArrayList();

            Join<ProductEntity, CompanyEntity> company = root.join(OWNER);
            predicatesAnd.add(builder.equal(company.get("cnpj"), document));

            return builder.and(builder.or(predicatesOr.toArray(new Predicate[]{})), builder.and(predicatesAnd.toArray(new Predicate[]{})));
        };
    }
}
