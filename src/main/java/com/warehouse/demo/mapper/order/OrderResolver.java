package com.warehouse.demo.mapper.order;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.order.Order;
import com.warehouse.demo.repository.order.OrderRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor
public class OrderResolver {
    private final OrderRepository orderRepository;

    public Order mapOrder(long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(EntityName.ORDER, OutputMessage.NOT_FOUND)));
    }
}
