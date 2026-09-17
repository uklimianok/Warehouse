package com.warehouse.demo.service.order.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
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
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;
import com.warehouse.demo.util.info.StatusInfo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderServiceImpl extends AbstractService<Order, Long> implements OrderService {
    private final OrderService self;

    private final OrderRepository orderRepository;
    private final OrderedProductRepository orderedProductRepository;
    private final OrderPalletRepository orderPalletRepository;
    private final ReturnProductRepository returnProductRepository;
    private final GateRepository gateRepository;
    private final StatusRepository statusRepository;

    private final OrderRequestMapper orderRequestMapper;

    public static final String GATE_REQUIRED = "must contain any gate.";

    public OrderServiceImpl(@Lazy OrderService self, OrderRepository orderRepository, OrderedProductRepository orderedProductRepository, OrderPalletRepository orderPalletRepository, ReturnProductRepository returnProductRepository, GateRepository gateRepository, StatusRepository statusRepository, OrderRequestMapper orderRequestMapper) {
        this.self = self;   // Set only when it is used, not when declared
        this.orderRepository = orderRepository;
        this.orderedProductRepository = orderedProductRepository;
        this.orderPalletRepository = orderPalletRepository;
        this.returnProductRepository = returnProductRepository;
        this.gateRepository = gateRepository;
        this.statusRepository = statusRepository;
        this.orderRequestMapper = orderRequestMapper;
    }

    @Override 
    @Cacheable(value = "orders", key = "#id")
    public Order read(Long id) {
        return super.read(id);
    }

    @Override
    public Order create(OrderRequest orderRequest) {
        Order order = new Order();
        order.setStatus(statusRepository
            .findByNameAndType(StatusInfo.OrderStatus.ACCEPTED.getName(), Entity.ORDER.getEntity())
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)))  
        );

        return modifyAndSave(order, orderRequest);
    }

    @Override
    @CacheEvict(value = "orders", key = "#id")
    public Order update(long id, OrderRequest orderRequest) {
        Order order = self.read(id);    // Cached object is provided through proxy "self", not through direct "this"
        order.setStatus(statusRepository
            .findByIdAndType(orderRequest.getStatusId(), Entity.ORDER.getEntity())
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)))
        );

        if (orderRequest.getGateId() != null)
            order.setGate(gateRepository.findById(orderRequest.getGateId())
                .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(Entity.GATE, OutputMessage.NOT_FOUND)))
            );
        else
            order.setGate(null);

        if (!order.getStatus().getName().equals(StatusInfo.OrderStatus.ACCEPTED.getName()) && order.getGate() == null)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), GATE_REQUIRED));

        return modifyAndSave(order, orderRequest);
    }

    @Override 
    @CacheEvict(value = "orders", key = "#id")
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    protected JpaRepository<Order, Long> getRepository() {
        return orderRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.ORDER;
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
