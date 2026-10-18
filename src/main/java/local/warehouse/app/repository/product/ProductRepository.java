package local.warehouse.app.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByBarcodeNumber(String barcodeNumber);
    
    boolean existsByProducerId(long producerId);
}
