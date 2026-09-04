package com.warehouse.demo.mapper.item.paperCard;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.item.paperCard.PaperCardResponse;
import com.warehouse.demo.entity.item.PaperCard;
import com.warehouse.demo.mapper.order.orderPallet.OrderPalletResponseMapper;

@Mapper(componentModel = "spring", uses = OrderPalletResponseMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PaperCardResponseMapper {
    PaperCardResponse convertToResponse(PaperCard paperCard);
}
