package local.warehouse.app.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.order.ReturnProduct;

public interface ReturnProductRepository extends JpaRepository<ReturnProduct, Long> {

    boolean existsByProductId(long productId);
    
    boolean existsByOrderId(long orderId);
}
