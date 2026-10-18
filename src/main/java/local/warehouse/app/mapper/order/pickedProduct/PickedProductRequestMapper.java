package local.warehouse.app.mapper.order.pickedProduct;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.order.pickedProduct.PickedProductRequest;
import local.warehouse.app.entity.order.PickedProduct;
import local.warehouse.app.mapper.order.orderPallet.OrderPalletResolver;
import local.warehouse.app.mapper.product.productPackage.ProductPackageResolver;

@Mapper(
    componentModel = "spring", 
    uses = {OrderPalletResolver.class, ProductPackageResolver.class}, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR, 
    builder = @Builder(disableBuilder = true)
)
public interface PickedProductRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderPallet", source = "pickedProductRequest.orderPalletId")
    @Mapping(target = "productPackage", source = "pickedProductRequest.packageId")
    void convertFromRequest(PickedProductRequest pickedProductRequest, @MappingTarget PickedProduct pickedProduct);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderPallet", ignore = true)
    @Mapping(target = "productPackage", ignore = true)
    void convertFromGoodsPickerRequest(PickedProductRequest pickedProductRequest, @MappingTarget PickedProduct pickedProduct);
}
