package local.warehouse.app.mapper.product.productPallet;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.product.productPallet.ProductPalletRequest;
import local.warehouse.app.entity.product.ProductPallet;
import local.warehouse.app.mapper.item.pallet.PalletResolver;
import local.warehouse.app.mapper.product.productPackage.ProductPackageResolver;
import local.warehouse.app.mapper.service.status.StatusResolver;
import local.warehouse.app.mapper.workplace.workStation.WorkStationResolver;

@Mapper(
    componentModel = "spring", 
    uses = {ProductPackageResolver.class, PalletResolver.class, StatusResolver.class, WorkStationResolver.class}, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR, 
    builder = @Builder(disableBuilder = true)
)
public interface ProductPalletRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productPackage", source = "productPalletRequest.productPackageId")
    @Mapping(target = "pallet", source = "productPalletRequest.palletId")
    @Mapping(target = "palletNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "workStation", ignore = true)
    @Mapping(target = "nextWorkStation", ignore = true)
    void convertFromRequest(ProductPalletRequest productPalletRequest, @MappingTarget ProductPallet productPallet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productPackage", ignore = true)
    @Mapping(target = "packageAmount", ignore = true)
    @Mapping(target = "pallet", source = "productPalletRequest.palletId")
    @Mapping(target = "palletNumber", ignore = true)
    @Mapping(target = "groupNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "workStation", ignore = true)
    @Mapping(target = "nextWorkStation", ignore = true)
    void convertFromGoodsUnloaderRequest(ProductPalletRequest productPalletRequest, @MappingTarget ProductPallet productPallet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productPackage", ignore = true)
    @Mapping(target = "packageAmount", ignore = true)
    @Mapping(target = "pallet", ignore = true)
    @Mapping(target = "palletNumber", ignore = true)
    @Mapping(target = "groupNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "workStation", ignore = true)
    @Mapping(target = "nextWorkStation", ignore = true)
    void convertFromOperatorRequest(ProductPalletRequest productPalletRequest, @MappingTarget ProductPallet productPallet);
}
