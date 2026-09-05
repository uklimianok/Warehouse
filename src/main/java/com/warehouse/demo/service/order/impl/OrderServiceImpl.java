package com.warehouse.demo.service.order.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.order.OrderRequest;
import com.warehouse.demo.entity.order.Order;
import com.warehouse.demo.mapper.order.OrderRequestMapper;
import com.warehouse.demo.repository.order.OrderPalletRepository;
import com.warehouse.demo.repository.order.OrderRepository;
import com.warehouse.demo.repository.order.OrderedProductRepository;
import com.warehouse.demo.repository.order.ReturnProductRepository;
import com.warehouse.demo.repository.service.StatusRepository;
import com.warehouse.demo.repository.workplace.GateRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.order.OrderService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.StatusInfo;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends AbstractService<Order, Long> implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderedProductRepository orderedProductRepository;
    private final OrderPalletRepository orderPalletRepository;
    private final ReturnProductRepository returnProductRepository;
    private final GateRepository gateRepository;
    private final StatusRepository statusRepository;

    private final OrderRequestMapper orderRequestMapper;

    public static final String GATE_REQUIRED = "must contain any gate.";

    @Override
    public Order create(OrderRequest orderRequest) {
        Order order = new Order();
        order.setStatus(statusRepository
            .findByNameAndType(StatusInfo.OrderStatus.ACCEPTED.getName(), EntityName.ORDER.getName())
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(EntityName.STATUS, OutputMessage.NOT_FOUND)))  
        );

        return modifyAndSave(order, orderRequest);
    }

    @Override
    public Order update(long id, OrderRequest orderRequest) {
        Order order = read(id);
        order.setStatus(statusRepository
            .findByIdAndType(orderRequest.getStatusId(), EntityName.ORDER.getName())
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(EntityName.STATUS, OutputMessage.NOT_FOUND)))
        );

        if (orderRequest.getGateId() != null)
            order.setGate(gateRepository.findById(orderRequest.getGateId())
                .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(EntityName.GATE, OutputMessage.NOT_FOUND)))
            );
        else
            order.setGate(null);

        if (!order.getStatus().getName().equals(StatusInfo.OrderStatus.ACCEPTED.getName()) && order.getGate() == null)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), GATE_REQUIRED));

        return modifyAndSave(order, orderRequest);
    }

    @Override
    protected JpaRepository<Order, Long> getRepository() {
        return orderRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.ORDER;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInOrderedProduct = orderedProductRepository.existsByOrderId(id);
        boolean activeInOrderPallet = orderPalletRepository.existsByOrderId(id);
        boolean activeInReturnProduct = returnProductRepository.existsByOrderId(id);
        return activeInOrderedProduct || activeInOrderPallet || activeInReturnProduct;
    }

    private Order modifyAndSave(Order target, OrderRequest from) {
        orderRequestMapper.convertFromRequest(from, target);
        return orderRepository.save(target);
    }
}
