package com.warehouse.demo.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {}
