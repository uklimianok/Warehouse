package local.warehouse.app.mapper.item.pallet;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.item.pallet.PalletRequest;
import local.warehouse.app.entity.item.Pallet;

@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface PalletRequestMapper {
    @Mapping(target = "id", ignore = true)
    void convertFromRequest(PalletRequest palletRequest, @MappingTarget Pallet pallet);
}
