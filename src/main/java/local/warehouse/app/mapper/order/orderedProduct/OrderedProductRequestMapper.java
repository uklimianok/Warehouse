package local.warehouse.app.mapper.order.orderedProduct;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.order.orderedProduct.OrderedProductRequest;
import local.warehouse.app.entity.order.OrderedProduct;
import local.warehouse.app.mapper.order.OrderResolver;
import local.warehouse.app.mapper.product.productPackage.ProductPackageResolver;

@Mapper(
    componentModel = "spring", 
    uses = {OrderResolver.class, ProductPackageResolver.class}, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR, 
    builder = @Builder(disableBuilder = true)
)
public interface OrderedProductRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "orderedProductRequest.orderId")
    @Mapping(target = "productPackage", source = "orderedProductRequest.packageId")
    void convertFromRequest(OrderedProductRequest orderedProductRequest, @MappingTarget OrderedProduct orderedProduct);
}
