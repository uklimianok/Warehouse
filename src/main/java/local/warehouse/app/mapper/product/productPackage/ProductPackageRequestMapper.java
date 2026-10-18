package local.warehouse.app.mapper.product.productPackage;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.product.productPackage.ProductPackageRequest;
import local.warehouse.app.entity.product.ProductPackage;
import local.warehouse.app.mapper.product.ProductResolver;

@Mapper(
    componentModel = "spring", 
    uses = ProductResolver.class, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR, 
    builder = @Builder(disableBuilder = true)
)
public interface ProductPackageRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", source = "productPackageRequest.productId")
    void convertFromRequest(ProductPackageRequest productPackageRequest, @MappingTarget ProductPackage productPackage);
}
