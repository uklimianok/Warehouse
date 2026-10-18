package local.warehouse.app.service.product;

import local.warehouse.app.configuration.security.UserPrincipal;
import local.warehouse.app.dto.product.productPallet.ProductPalletRequest;
import local.warehouse.app.entity.product.ProductPallet;
import local.warehouse.app.service.BaseService;

public interface ProductPalletService extends BaseService<ProductPallet, Long> {
    ProductPallet create(ProductPalletRequest productPalletRequest);
    ProductPallet update(long id, ProductPalletRequest productPalletRequest, UserPrincipal userPrincipal);
}
