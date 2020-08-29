package io.github.blackfishlabs.forza.order.repository;

import io.github.blackfishlabs.forza.order.model.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long>, JpaSpecificationExecutor<OrderItemEntity> {

    @Modifying
    @Query(value = "SELECT " +
            "APP_ORDER_ITEM.* " +
            "FROM APP_ORDER_ITEM " +
            "JOIN APP_ORDER O ON O.ID = APP_ORDER_ITEM.ORDER_ID " +
            "WHERE O.ID = :code " +
            "GROUP BY APP_ORDER_ITEM.ID", nativeQuery = true)
    List<OrderItemEntity> findOrderItemMobile(@Param("code") Long idOrder);
}
