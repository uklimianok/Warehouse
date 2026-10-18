package local.warehouse.app.service.product;

import local.warehouse.app.dto.product.ProductRequest;
import local.warehouse.app.entity.product.Product;
import local.warehouse.app.service.BaseService;

public interface ProductService extends BaseService<Product, Long> {
    Product create(ProductRequest productRequest);
    Product update(long id, ProductRequest productRequest);
}
