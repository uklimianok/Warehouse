package local.warehouse.app.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.order.PickedProduct;

public interface PickedProductRepository extends JpaRepository<PickedProduct, Long> {

    boolean existsByProductPackageId(long productPackageId);

    boolean existsByOrderPalletId(long orderPalletId);
    
    boolean existsByOrderPalletIdAndCompleted(long orderPalletId, boolean isCompleted);
}
