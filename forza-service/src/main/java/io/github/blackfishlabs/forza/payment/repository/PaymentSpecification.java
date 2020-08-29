package io.github.blackfishlabs.forza.payment.repository;

import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.payment.model.PaymentEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PaymentSpecification {

    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String OWNER = "owner";

    public static Specification<PaymentEntity> findByCompany(final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            Join<PaymentEntity, CompanyEntity> company = root.join(OWNER);

            predicates.add(builder.equal(company.get("cnpj"), document));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }

    public static Specification<PaymentEntity> findByCompanyAndLastUpdate(final String document, final String lastUpdateTime) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            Join<PaymentEntity, CompanyEntity> company = root.join(OWNER);

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

}
