package com.warehouse.demo.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.product.ProductPallet;

public interface ProductPalletRepository extends JpaRepository<ProductPallet, Long> {}
