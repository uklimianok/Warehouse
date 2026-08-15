package com.warehouse.demo.service.order.impl;

import com.warehouse.demo.repository.service.StatusRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.order.orderPallet.OrderPalletRequest;
import com.warehouse.demo.entity.order.OrderPallet;
import com.warehouse.demo.repository.item.PalletRepository;
import com.warehouse.demo.repository.item.PaperCardRepository;
import com.warehouse.demo.repository.order.OrderPalletRepository;
import com.warehouse.demo.repository.order.OrderRepository;
import com.warehouse.demo.repository.order.PickedProductRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.order.OrderPalletService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.StatusInfo;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderPalletServiceImpl extends AbstractService<OrderPallet, Long> implements OrderPalletService {
    private final StatusRepository statusRepository;
    private final OrderPalletRepository orderPalletRepository;
    private final PaperCardRepository paperCardRepository;
    private final PickedProductRepository pickedProductRepository;
    private final OrderRepository orderRepository;
    private final PalletRepository palletRepository;

    @Override
    public OrderPallet create(OrderPalletRequest orderPalletRequest) {
        OrderPallet orderPallet = new OrderPallet();
        orderPallet.setStatus(
            statusRepository.findByNameAndType(StatusInfo.OrderPalletStatus.PICKING.getName(), getEntityName().getName())
                .orElseThrow(() ->
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.STATUS, OutputMessage.NOT_FOUND)) 
            )   
        );

        return modifyAndSave(orderPallet, orderPalletRequest);
    }

    @Override
    public OrderPallet update(long id, OrderPalletRequest orderPalletRequest) {
        OrderPallet orderPallet = read(id);
        orderPallet.setStatus(
            statusRepository.findByIdAndType(orderPalletRequest.getStatusId(), getEntityName().getName())
                .orElseThrow(() ->
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.STATUS, OutputMessage.NOT_FOUND))            
            )
        );

        return modifyAndSave(orderPallet, orderPalletRequest);
    }

    @Override
    protected JpaRepository<OrderPallet, Long> getRepository() {
        return orderPalletRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.ORDER_PALLET;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInPaperCard = paperCardRepository.existsByOrderPalletId(id);
        boolean activeInPickedProduct = pickedProductRepository.existsByOrderPalletId(id);
        return activeInPaperCard || activeInPickedProduct;
    }

    private OrderPallet modifyAndSave(OrderPallet target, OrderPalletRequest from) {
        target.setOrder(
            orderRepository.findById(from.getOrderId())
                .orElseThrow(() -> 
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.ORDER, OutputMessage.NOT_FOUND))
            )
        );
        target.setPallet(
            palletRepository.findById(from.getPalletId())
                .orElseThrow(() -> 
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.PALLET, OutputMessage.NOT_FOUND))
            )
        );

        return orderPalletRepository.save(target);
    }
}
