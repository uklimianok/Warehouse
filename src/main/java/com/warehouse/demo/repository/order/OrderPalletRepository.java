package com.warehouse.demo.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.order.OrderPallet;

public interface OrderPalletRepository extends JpaRepository<OrderPallet, Long> {
    boolean existsByPalletId(long palletId);
    boolean existsByStatusId(long statusId);
    boolean existsByOrderId(long orderId);
}
