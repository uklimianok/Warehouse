package local.warehouse.app.mapper.product.productPackage;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.product.productPackage.ProductPackageResponse;
import local.warehouse.app.entity.product.ProductPackage;
import local.warehouse.app.mapper.product.ProductResponseMapper;

@Mapper(
    componentModel = "spring", 
    uses = ProductResponseMapper.class, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR, 
    builder = @Builder(disableBuilder = true)
)
public interface ProductPackageResponseMapper {
    ProductPackageResponse convertToResponse(ProductPackage productPackage);
}
