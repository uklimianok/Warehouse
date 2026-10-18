package local.warehouse.app.mapper.item.paperCard;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.item.paperCard.PaperCardResponse;
import local.warehouse.app.entity.item.PaperCard;
import local.warehouse.app.mapper.order.orderPallet.OrderPalletResponseMapper;

@Mapper(
    componentModel = "spring", 
    uses = OrderPalletResponseMapper.class, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PaperCardResponseMapper {
    PaperCardResponse convertToResponse(PaperCard paperCard);
}
