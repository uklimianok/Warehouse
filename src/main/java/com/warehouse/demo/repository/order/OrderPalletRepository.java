package com.warehouse.demo.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.warehouse.demo.entity.order.OrderPallet;

public interface OrderPalletRepository extends JpaRepository<OrderPallet, Long> {
    boolean existsByPalletId(long palletId);
    boolean existsByStatusId(long statusId);
    boolean existsByOrderId(long orderId);
    int updateStatusById(long id, long statusId);
    @Query(
        value = "SELECT NOT EXISTS (SELECT 1 FROM ordered_products op WHERE NOT EXISTS (SELECT 1 FROM picked_products pp WHERE op.package_id = pp.package_id))",
        nativeQuery = true
    ) 
    boolean isAllOrderedProductExistInPickedProduct();
}
