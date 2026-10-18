package local.warehouse.app.mapper.order.returnProduct;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.order.returnProduct.ReturnProductRequest;
import local.warehouse.app.entity.order.ReturnProduct;
import local.warehouse.app.mapper.order.OrderResolver;
import local.warehouse.app.mapper.product.ProductResolver;

@Mapper(
    componentModel = "spring", 
    uses = {OrderResolver.class, ProductResolver.class}, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR, 
    builder = @Builder(disableBuilder = true)
)
public interface ReturnProductRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "returnProductRequest.orderId")
    @Mapping(target = "product", source = "returnProductRequest.productId")
    void convertFromRequest(ReturnProductRequest returnProductRequest, @MappingTarget ReturnProduct returnProduct);
}
