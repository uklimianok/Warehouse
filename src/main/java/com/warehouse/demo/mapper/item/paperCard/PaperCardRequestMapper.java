package com.warehouse.demo.mapper.item.paperCard;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.item.paperCard.PaperCardRequest;
import com.warehouse.demo.entity.item.PaperCard;
import com.warehouse.demo.mapper.order.orderPallet.OrderPalletResolver;

@Mapper(componentModel = "spring", uses = OrderPalletResolver.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PaperCardRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderPallet", source = "paperCardRequest.orderPalletId")
    void convertFromRequest(PaperCardRequest paperCardRequest, @MappingTarget PaperCard paperCard);
}
