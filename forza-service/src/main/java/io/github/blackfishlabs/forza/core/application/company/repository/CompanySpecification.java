package io.github.blackfishlabs.forza.core.application.company.repository;

import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.helper.FormattingUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.List;

public class CompanySpecification {

    private CompanySpecification() {
    }

    public static Specification<CompanyEntity> findByCompany(final String document) {

        return (root, query, builder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            predicates.add(builder.equal(root.get("cnpj"), FormattingUtils.formatCpforCnpj(document)));

            return builder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
