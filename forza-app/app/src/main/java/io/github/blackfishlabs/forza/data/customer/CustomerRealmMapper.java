package io.github.blackfishlabs.forza.data.customer;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmMapper;
import io.github.blackfishlabs.forza.domain.entity.CustomerEntity;
import io.github.blackfishlabs.forza.domain.pojo.Customer;

public class CustomerRealmMapper extends RealmMapper<Customer, CustomerEntity> {

    @Override
    public CustomerEntity toEntity(final Customer object) {
        return new CustomerEntity()
                .withId(object.getId())
                .withCustomerId(object.getCustomerId())
                .withCode(object.getCode())
                .withName(object.getName())
                .withFantasyName(object.getFantasyName())
                .withType(object.getType())
                .withCpfOrCnpj(object.getCpfOrCnpj())
                .withContact(object.getContact())
                .withEmail(object.getEmail())
                .withMainPhone(object.getMainPhone())
                .withSecondaryPhone(object.getSecondaryPhone())
                .withAddress(object.getAddress())
                .withPostalCode(object.getPostalCode())
                .withDistrict(object.getDistrict())
                .withAddressNumber(object.getAddressNumber())
                .withAddressComplement(object.getAddressComplement())
                .withDefaultPriceTable(object.getDefaultPriceTable())
                .withCity(object.getCity() != null ? LocalDataInjector.cityMapper().toEntity(object.getCity()) : null)
                .withActive(object.isActive())
                .withLastChangeTime(object.getLastChangeTime())
                .withIe(object.getIe())
                .withBuyer(object.getBuyer())
                .withStatus(object.getStatus());
    }

    @Override
    public Customer toViewObject(final CustomerEntity entity) {
        return new Customer()
                .withId(entity.getId())
                .withCustomerId(entity.getCustomerId())
                .withCode(entity.getCode())
                .withName(entity.getName())
                .withFantasyName(entity.getFantasyName())
                .withType(entity.getType())
                .withCpfOrCnpj(entity.getCpfOrCnpj())
                .withContact(entity.getContact())
                .withEmail(entity.getEmail())
                .withMainPhone(entity.getMainPhone())
                .withSecondaryPhone(entity.getSecondaryPhone())
                .withAddress(entity.getAddress())
                .withPostalCode(entity.getPostalCode())
                .withDistrict(entity.getDistrict())
                .withAddressNumber(entity.getAddressNumber())
                .withAddressComplement(entity.getAddressComplement())
                .withDefaultPriceTable(entity.getDefaultPriceTable())
                .withCity(entity.getCity() != null ? LocalDataInjector.cityMapper().toViewObject(entity.getCity()) : null)
                .withActive(entity.isActive())
                .withLastChangeTime(entity.getLastChangeTime())
                .withIe(entity.getIe())
                .withBuyer(entity.getBuyer())
                .withStatus(entity.getStatus());
    }
}
