package com.warehouse.demo.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.product.ProductPackage;

public interface ProductPackageRepository extends JpaRepository<ProductPackage, Long> {
    boolean existsByProductId(long id);
}
