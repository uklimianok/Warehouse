package local.warehouse.app.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.order.OrderedProduct;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {

    boolean existsByProductPackageId(long productPackageId);
    
    boolean existsByOrderId(long orderId);
}
