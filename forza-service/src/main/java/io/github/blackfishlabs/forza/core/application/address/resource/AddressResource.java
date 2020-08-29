package io.github.blackfishlabs.forza.core.application.address.resource;

import io.github.blackfishlabs.forza.core.application.address.converter.AddressConverter;
import io.github.blackfishlabs.forza.core.application.address.json.AddressJson;
import io.github.blackfishlabs.forza.core.application.address.model.AddressEntity;
import io.github.blackfishlabs.forza.core.application.address.service.AddressService;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ResourcePaths.ADDRESS_PATH)
public class AddressResource implements APIMap {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressConverter addressConverter;

    @RequestMapping(method = {RequestMethod.POST})
    public AddressJson save(@RequestBody AddressJson json) {
        AddressEntity entity = addressService.insert(addressConverter.convertFrom(json));

        return addressConverter.convertFrom(entity);
    }

    @RequestMapping(method = {RequestMethod.DELETE})
    public void delete(@RequestBody AddressJson json) {
        addressService.delete(addressConverter.convertFrom(json));
    }

    @RequestMapping(method = {RequestMethod.PUT})
    public void update(@RequestBody AddressJson json) {
        addressService.update(addressConverter.convertFrom(json));
    }

    @RequestMapping(method = {RequestMethod.GET})
    public List<AddressJson> getAll() {
        return addressConverter.convertListModelFrom(addressService.findAll());
    }

}
