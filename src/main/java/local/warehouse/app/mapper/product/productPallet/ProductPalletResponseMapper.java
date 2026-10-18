package local.warehouse.app.mapper.product.productPallet;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.product.productPallet.FullProductPalletResponse;
import local.warehouse.app.dto.product.productPallet.ProductPalletResponse;
import local.warehouse.app.dto.product.productPallet.TransferProductPalletResponse;
import local.warehouse.app.entity.product.ProductPallet;
import local.warehouse.app.mapper.item.pallet.PalletResponseMapper;
import local.warehouse.app.mapper.product.productPackage.ProductPackageResponseMapper;
import local.warehouse.app.mapper.service.status.StatusResponseMapper;
import local.warehouse.app.mapper.workplace.workStation.WorkStationResponseMapper;

@Mapper(
    componentModel = "spring", 
    uses = {ProductPackageResponseMapper.class, PalletResponseMapper.class, StatusResponseMapper.class, WorkStationResponseMapper.class}, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ProductPalletResponseMapper {
    ProductPalletResponse convertToResponse(ProductPallet productPallet);
    TransferProductPalletResponse convertToTransferResponse(ProductPallet productPallet);
    FullProductPalletResponse convertToFullResponse(ProductPallet productPallet);
}
