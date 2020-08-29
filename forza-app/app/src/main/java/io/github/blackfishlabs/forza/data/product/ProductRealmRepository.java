package io.github.blackfishlabs.forza.data.product;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmRepository;
import io.github.blackfishlabs.forza.domain.entity.ProductEntity;
import io.github.blackfishlabs.forza.domain.pojo.Product;

public class ProductRealmRepository extends RealmRepository<Product, ProductEntity> implements ProductRepository {

    public ProductRealmRepository() {
        super(ProductEntity.class, LocalDataInjector.productMapper());
    }
}
