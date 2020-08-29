package io.github.blackfishlabs.forza.core.application.phone.resource;

import io.github.blackfishlabs.forza.core.application.phone.converter.PhoneConverter;
import io.github.blackfishlabs.forza.core.application.phone.json.PhoneJson;
import io.github.blackfishlabs.forza.core.application.phone.model.PhoneEntity;
import io.github.blackfishlabs.forza.core.application.phone.service.PhoneService;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ResourcePaths.PHONE_PATH)
public class PhoneResource implements APIMap {

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private PhoneConverter phoneConverter;

    @RequestMapping(method = {RequestMethod.POST})
    public PhoneJson save(@RequestBody PhoneJson json) {
        PhoneEntity entity = phoneService.insert(phoneConverter.convertFrom(json));

        return phoneConverter.convertFrom(entity);
    }

    @RequestMapping(method = {RequestMethod.DELETE})
    public void delete(@RequestBody PhoneJson json) {
        phoneService.delete(phoneConverter.convertFrom(json));
    }

    @RequestMapping(method = {RequestMethod.PUT})
    public void update(@RequestBody PhoneJson json) {
        phoneService.update(phoneConverter.convertFrom(json));
    }

    @RequestMapping(method = {RequestMethod.GET})
    public List<PhoneJson> getAll() {
        return phoneConverter.convertListModelFrom(phoneService.findAll());
    }
}
