package local.warehouse.app.mapper.order.orderPallet;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.order.OrderPallet;
import local.warehouse.app.repository.order.OrderPalletRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class OrderPalletResolver {
    private final OrderPalletRepository orderPalletRepository;

    public OrderPallet mapOrderPallet(long orderPalletId) {
        return orderPalletRepository.findById(orderPalletId)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageHandler.getOutputMessage(Entity.ORDER_PALLET, OutputMessage.NOT_FOUND)));
    }
}