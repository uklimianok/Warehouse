package local.warehouse.app.service.product;

import local.warehouse.app.dto.product.productPackage.ProductPackageRequest;
import local.warehouse.app.entity.product.ProductPackage;
import local.warehouse.app.service.BaseService;

public interface ProductPackageService extends BaseService<ProductPackage, Long> {
    ProductPackage create(ProductPackageRequest productPackageRequest);
    ProductPackage update(long id, ProductPackageRequest productPackageRequest);
}
