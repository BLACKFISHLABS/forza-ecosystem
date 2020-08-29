package io.github.blackfishlabs.forza.pricetable.repository;

import io.github.blackfishlabs.forza.pricetable.model.PriceTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceTableRepository extends JpaRepository<PriceTableEntity, Long>, JpaSpecificationExecutor<PriceTableEntity> {

    @Modifying
    @Query(value = "select my_price_table.* from APP_CHARGE charge " +
            "join APP_CHARGE_BLOCK block on block.id = charge.charge_block_id " +
            "join APP_CHARGE_TABLE_PRICE  price_table on price_table.charge_block_id = block.id " +
            "join APP_PRICE_TABLE my_price_table on my_price_table.code = price_table.code " +
            "join APP_COMPANY  company on company.cnpj = :cnpj " +
            "where charge.code = :code", nativeQuery = true)
    List<PriceTableEntity> findByCompanyAndCharge(@Param("cnpj") String cnpj, @Param("code") String code);
}
