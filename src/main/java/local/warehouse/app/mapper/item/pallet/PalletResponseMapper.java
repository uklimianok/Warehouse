package local.warehouse.app.mapper.item.pallet;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.item.pallet.FullPalletResponse;
import local.warehouse.app.dto.item.pallet.PalletResponse;
import local.warehouse.app.entity.item.Pallet;

@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface PalletResponseMapper {
    PalletResponse convertToResponse(Pallet pallet);
    FullPalletResponse convertToFullResponse(Pallet pallet);
}
