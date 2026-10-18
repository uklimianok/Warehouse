package local.warehouse.app.mapper.order;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.order.Order;
import local.warehouse.app.repository.order.OrderRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor
public class OrderResolver {
    private final OrderRepository orderRepository;

    public Order mapOrder(long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageHandler.getOutputMessage(Entity.ORDER, OutputMessage.NOT_FOUND)));
    }
}
