package local.warehouse.app.mapper.product;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.product.FullProductResponse;
import local.warehouse.app.dto.product.ProductResponse;
import local.warehouse.app.entity.product.Product;
import local.warehouse.app.mapper.employee.organization.OrganizationResponseMapper;

@Mapper(
    componentModel = "spring", 
    uses = OrganizationResponseMapper.class, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ProductResponseMapper {
    ProductResponse convertToResponse(Product product);
    FullProductResponse convertToFullResponse(Product product);
}
