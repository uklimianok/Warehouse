package com.warehouse.demo.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.order.ReturnProduct;

public interface ReturnProductRepository extends JpaRepository<ReturnProduct, Long> {}
