package io.github.blackfishlabs.forza.data.product;

import io.github.blackfishlabs.forza.data.realm.RealmMapper;
import io.github.blackfishlabs.forza.domain.entity.ProductEntity;
import io.github.blackfishlabs.forza.domain.pojo.Product;

public class ProductRealmMapper extends RealmMapper<Product, ProductEntity> {

    @Override
    public ProductEntity toEntity(final Product object) {
        return new ProductEntity()
                .withProductId(object.getProductId())
                .withCode(object.getCode())
                .withBarCode(object.getBarCode())
                .withDescription(object.getDescription())
                .withUnit(object.getUnit())
                .withGroup(object.getGroup())
                .withObservation(object.getObservation())
                .withActive(object.isActive())
                .withLastChangeTime(object.getLastChangeTime());
    }

    @Override
    public Product toViewObject(final ProductEntity entity) {
        return new Product()
                .withProductId(entity.getProductId())
                .withCode(entity.getCode())
                .withBarCode(entity.getBarCode())
                .withDescription(entity.getDescription())
                .withUnit(entity.getUnit())
                .withGroup(entity.getGroup())
                .withObservation(entity.getObservation())
                .withActive(entity.isActive())
                .withLastChangeTime(entity.getLastChangeTime());
    }
}
