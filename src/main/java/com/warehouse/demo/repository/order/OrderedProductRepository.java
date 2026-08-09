package com.warehouse.demo.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.order.OrderedProduct;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {
    boolean existsByProductPackageId(long id);
}
