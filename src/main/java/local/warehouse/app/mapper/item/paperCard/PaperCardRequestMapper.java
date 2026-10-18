package local.warehouse.app.mapper.item.paperCard;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.item.paperCard.PaperCardRequest;
import local.warehouse.app.entity.item.PaperCard;
import local.warehouse.app.mapper.order.orderPallet.OrderPalletResolver;

@Mapper(
    componentModel = "spring", 
    uses = OrderPalletResolver.class, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR, 
    builder = @Builder(disableBuilder = true)
)
public interface PaperCardRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderPallet", source = "paperCardRequest.orderPalletId")
    void convertFromRequest(PaperCardRequest paperCardRequest, @MappingTarget PaperCard paperCard);
}
