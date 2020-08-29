package io.github.blackfishlabs.forza.core.application.contact.repository;

import io.github.blackfishlabs.forza.core.application.contact.model.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContactRepository extends JpaRepository<ContactEntity, Long>, JpaSpecificationExecutor<ContactEntity> {
}
