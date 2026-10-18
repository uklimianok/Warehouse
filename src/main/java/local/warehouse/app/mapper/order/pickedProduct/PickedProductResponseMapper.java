package local.warehouse.app.mapper.order.pickedProduct;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.order.pickedProduct.PickedProductResponse;
import local.warehouse.app.entity.order.PickedProduct;
import local.warehouse.app.mapper.order.orderPallet.OrderPalletResponseMapper;
import local.warehouse.app.mapper.product.productPackage.ProductPackageResponseMapper;

@Mapper(
    componentModel = "spring", 
    uses = {OrderPalletResponseMapper.class, ProductPackageResponseMapper.class}, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PickedProductResponseMapper {
    PickedProductResponse convertToResponse(PickedProduct pickedProduct);
}
