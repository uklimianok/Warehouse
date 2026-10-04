package com.warehouse.demo.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.warehouse.demo.entity.order.OrderPallet;

public interface OrderPalletRepository extends JpaRepository<OrderPallet, Long> {
    boolean existsByPalletId(long palletId);
    boolean existsByStatusId(long statusId);
    boolean existsByOrderId(long orderId);
    @Query(
        value = "SELECT NOT EXISTS (SELECT 1 FROM ordered_products op WHERE op.order_id = :orderId AND NOT EXISTS (SELECT 1 FROM picked_products pp JOIN order_pallets opl ON opl.id = pp.order_pallet_id WHERE opl.order_id = :orderId AND pp.package_id = op.package_id))",
        nativeQuery = true
    ) 
    boolean areAllOrderedProductExistInPickedProduct(long orderId);
    @Query("SELECT o.id FROM OrderPallet o WHERE o.order.id = :orderId AND o.status.id = :statusId")
    boolean areAllInOrderIdHaveStatusId(long orderId,long statusId);
}
