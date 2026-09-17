package com.warehouse.demo.mapper.order;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.order.Order;
import com.warehouse.demo.repository.order.OrderRepository;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor
public class OrderResolver {
    private final OrderRepository orderRepository;

    public Order mapOrder(long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(Entity.ORDER, OutputMessage.NOT_FOUND)));
    }
}
