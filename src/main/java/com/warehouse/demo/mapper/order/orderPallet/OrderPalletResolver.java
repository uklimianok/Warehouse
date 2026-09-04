package com.warehouse.demo.mapper.order.orderPallet;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.order.OrderPallet;
import com.warehouse.demo.repository.order.OrderPalletRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class OrderPalletResolver {
    private final OrderPalletRepository orderPalletRepository;

    public OrderPallet mapOrderPallet(long orderPalletId) {
        return orderPalletRepository.findById(orderPalletId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(EntityName.ORDER_PALLET, OutputMessage.NOT_FOUND)));
    }
}