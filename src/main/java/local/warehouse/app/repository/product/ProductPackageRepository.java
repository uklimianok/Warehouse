package local.warehouse.app.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.product.ProductPackage;

public interface ProductPackageRepository extends JpaRepository<ProductPackage, Long> {
    
    boolean existsByProductId(long productId);
}
