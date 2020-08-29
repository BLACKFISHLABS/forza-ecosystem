package io.github.blackfishlabs.forza.core.application.contact.converter;

import io.github.blackfishlabs.forza.core.application.contact.json.ContactJson;
import io.github.blackfishlabs.forza.core.application.contact.model.ContactEntity;
import io.github.blackfishlabs.forza.core.application.phone.converter.PhoneConverter;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContactConverter extends Converter<ContactJson, ContactEntity> {

    @Autowired
    private PhoneConverter phoneConverter;

    @Override
    public ContactJson convertFrom(ContactEntity entity) {
        ContactJson json = new ContactJson();

        json.setId(entity.getId());
        json.setName(entity.getName());
        json.setPhoneJson(phoneConverter.convertFrom(entity.getPhone()));

        return json;
    }

    @Override
    public ContactEntity convertFrom(ContactJson json) {
        ContactEntity entity = new ContactEntity();

        entity.setId(json.getId());
        entity.setName(json.getName());
        entity.setPhone(phoneConverter.convertFrom(json.getPhoneJson()));

        return entity;
    }

}
