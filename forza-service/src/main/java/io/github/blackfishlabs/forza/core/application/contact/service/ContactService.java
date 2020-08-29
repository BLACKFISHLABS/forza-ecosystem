package io.github.blackfishlabs.forza.core.application.contact.service;

import io.github.blackfishlabs.forza.core.application.contact.model.ContactEntity;
import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import org.springframework.stereotype.Service;

@Service
public class ContactService extends GenericService<ContactEntity, Long> {
}
