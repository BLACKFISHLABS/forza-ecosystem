package io.github.blackfishlabs.forza.customer.repository;

import io.github.blackfishlabs.forza.customer.model.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>, JpaSpecificationExecutor<CustomerEntity> {

    @Modifying
    @Query(value = "select my_customer.* from APP_CHARGE charge " +
            "join APP_CHARGE_ROUTE route on route.charge_id = charge.id " +
            "join APP_CHARGE_CUSTOMER customer on customer.route_id = route.id " +
            "join APP_CUSTOMER my_customer on my_customer.code = customer.code " +
            "join APP_COMPANY  company on company.cnpj = :cnpj " +
            "where charge.code = :code", nativeQuery = true)
    List<CustomerEntity> findByCompanyAndCharge(@Param("cnpj") String cnpj, @Param("code") String code);


}
