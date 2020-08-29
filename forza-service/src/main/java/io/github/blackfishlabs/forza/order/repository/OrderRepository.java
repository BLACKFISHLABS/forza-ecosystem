package io.github.blackfishlabs.forza.order.repository;

import io.github.blackfishlabs.forza.order.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {

    @Modifying
    @Query(value = "SELECT DISTINCT " +
            "APP_ORDER.resume_code as resume_code, " +
            "C.CODE customer_code, " +
            "P.CODE payment_code, " +
            "COALESCE(SUM(COALESCE(OI.SUB_TOTAL, 0.0)), 0.0) total, " +
            "APP_ORDER.* " +
            "FROM APP_ORDER " +
            "INNER JOIN APP_PRICE_TABLE PT ON PT.ID = APP_ORDER.PRICETABLE_ID " +
            "AND PT.COMPANY_ID = :code " +
            "INNER JOIN APP_CHARGE_TABLE_PRICE CPT ON CPT.CODE = PT.CODE " +
            "INNER JOIN APP_CUSTOMER C ON C.ID = APP_ORDER.CUSTOMER_ID " +
            "INNER JOIN APP_PAYMENT P ON P.ID = APP_ORDER.PAYMENT_ID " +
            "INNER JOIN APP_ORDER_ITEM OI ON OI.ORDER_ID = APP_ORDER.ID " +
            "INNER JOIN APP_CHARGE_BLOCK CHB ON CHB.ID = CPT.CHARGE_BLOCK_ID " +
            "INNER JOIN APP_CHARGE CH ON CH.CHARGE_BLOCK_ID = CHB.ID " +
            "WHERE APP_ORDER.COMPANY_ID = :code " +
            "AND APP_ORDER.STATUS = :status " +
            "GROUP BY APP_ORDER.ID, CH.CODE, C.CODE, P.CODE " +
            "ORDER BY APP_ORDER.issue_date DESC ", nativeQuery = true)
    List<OrderEntity> findOrderMobile(@Param("code") Long code, @Param("status") String status);
}
