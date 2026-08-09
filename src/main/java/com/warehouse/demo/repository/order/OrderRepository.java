package com.warehouse.demo.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByStoreId(long id);
    boolean existsByShiftId(long id);
    boolean existsByStatusId(long id);
}
