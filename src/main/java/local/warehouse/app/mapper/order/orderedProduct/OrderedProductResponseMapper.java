package local.warehouse.app.mapper.order.orderedProduct;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.order.orderedProduct.OrderedProductResponse;
import local.warehouse.app.entity.order.OrderedProduct;
import local.warehouse.app.mapper.order.OrderResponseMapper;
import local.warehouse.app.mapper.product.productPackage.ProductPackageResponseMapper;

@Mapper(
    componentModel = "spring", 
    uses = {OrderResponseMapper.class, ProductPackageResponseMapper.class}, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface OrderedProductResponseMapper {
    OrderedProductResponse convertToResponse(OrderedProduct orderedProduct);
}
