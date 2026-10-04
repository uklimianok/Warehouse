package com.warehouse.demo.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.order.PickedProduct;

public interface PickedProductRepository extends JpaRepository<PickedProduct, Long> {
    boolean existsByProductPackageId(long productPackageId);
    boolean existsByOrderPalletId(long orderPalletId);
    boolean existsByOrderPalletIdAndIsCompleted(long orderPalletId, boolean isCompleted);
}
