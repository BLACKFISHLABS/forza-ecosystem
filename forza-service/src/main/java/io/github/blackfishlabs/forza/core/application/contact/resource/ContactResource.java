package io.github.blackfishlabs.forza.core.application.contact.resource;

import io.github.blackfishlabs.forza.core.application.contact.converter.ContactConverter;
import io.github.blackfishlabs.forza.core.application.contact.json.ContactJson;
import io.github.blackfishlabs.forza.core.application.contact.model.ContactEntity;
import io.github.blackfishlabs.forza.core.application.contact.service.ContactService;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ResourcePaths.CONTACT_PATH)
public class ContactResource implements APIMap {

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactConverter contactConverter;

    @RequestMapping(method = {RequestMethod.POST})
    public ContactJson save(@RequestBody ContactJson json) {
        ContactEntity entity = contactService.insert(contactConverter.convertFrom(json));
        return contactConverter.convertFrom(entity);
    }

    @RequestMapping(method = {RequestMethod.DELETE})
    public void delete(@RequestBody ContactJson json) {
        contactService.delete(contactConverter.convertFrom(json));
    }

    @RequestMapping(method = {RequestMethod.PUT})
    public void update(@RequestBody ContactJson json) {
        contactService.update(contactConverter.convertFrom(json));
    }

    @RequestMapping(method = {RequestMethod.GET})
    public List<ContactJson> getAll() {
        return contactConverter.convertListModelFrom(contactService.findAll());
    }

}
