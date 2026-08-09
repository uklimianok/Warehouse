package com.warehouse.demo.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByBarcodeNumber(String barcodeNumber);
}
