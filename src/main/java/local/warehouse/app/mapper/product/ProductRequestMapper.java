package local.warehouse.app.mapper.product;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.product.ProductRequest;
import local.warehouse.app.entity.product.Product;
import local.warehouse.app.mapper.employee.organization.OrganizationResolver;

@Mapper(
    componentModel = "spring", 
    uses = OrganizationResolver.class, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR, 
    builder = @Builder(disableBuilder = true)
)
public interface ProductRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producer", source = "productRequest.producerId")
    void convertFromRequest(ProductRequest productRequest, @MappingTarget Product product);
}