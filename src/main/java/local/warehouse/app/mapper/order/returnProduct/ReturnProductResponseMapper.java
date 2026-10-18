package local.warehouse.app.mapper.order.returnProduct;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.order.returnProduct.ReturnProductResponse;
import local.warehouse.app.entity.order.ReturnProduct;
import local.warehouse.app.mapper.order.OrderResponseMapper;
import local.warehouse.app.mapper.product.ProductResponseMapper;

@Mapper(
    componentModel = "spring", 
    uses = {OrderResponseMapper.class, ProductResponseMapper.class}, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ReturnProductResponseMapper {
    ReturnProductResponse convertToResponse(ReturnProduct returnProduct);
}
