package com.warehouse.demo.service.order.impl;

import com.warehouse.demo.repository.service.StatusRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.order.orderPallet.OrderPalletRequest;
import com.warehouse.demo.entity.order.OrderPallet;
import com.warehouse.demo.mapper.order.orderPallet.OrderPalletRequestMapper;
import com.warehouse.demo.repository.item.PaperCardRepository;
import com.warehouse.demo.repository.order.OrderPalletRepository;
import com.warehouse.demo.repository.order.PickedProductRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.order.OrderPalletService;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;
import com.warehouse.demo.util.info.StatusInfo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderPalletServiceImpl extends AbstractService<OrderPallet, Long> implements OrderPalletService {
    private final OrderPalletService self;

    private final StatusRepository statusRepository;
    private final OrderPalletRepository orderPalletRepository;
    private final PaperCardRepository paperCardRepository;
    private final PickedProductRepository pickedProductRepository;

    private final OrderPalletRequestMapper orderPalletRequestMapper;

    public OrderPalletServiceImpl(@Lazy OrderPalletService self, StatusRepository statusRepository, OrderPalletRepository orderPalletRepository, PaperCardRepository paperCardRepository, PickedProductRepository pickedProductRepository, OrderPalletRequestMapper orderPalletRequestMapper) {
        this.self = self;
        this.statusRepository = statusRepository;
        this.orderPalletRepository = orderPalletRepository;
        this.paperCardRepository = paperCardRepository;
        this.pickedProductRepository = pickedProductRepository;
        this.orderPalletRequestMapper = orderPalletRequestMapper;
    }

    @Override 
    @Cacheable(value = "orderPallets", key = "#id")
    public OrderPallet read(Long id) {
        return super.read(id);
    }

    @Override
    public OrderPallet create(OrderPalletRequest orderPalletRequest) {
        OrderPallet orderPallet = new OrderPallet();
        orderPallet.setStatus(
            statusRepository.findByNameAndType(StatusInfo.ORDER_PALLET_PICKING, getEntityName().getEntity())
                .orElseThrow(() ->
                    new EntityNotFoundException(Utility.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)) 
            )   
        );

        return modifyAndSave(orderPallet, orderPalletRequest);
    }

    @Override
    @CacheEvict(value = "orderPallets", key = "#id")
    public OrderPallet update(long id, OrderPalletRequest orderPalletRequest) {
        OrderPallet orderPallet = self.read(id);
        orderPallet.setStatus(
            statusRepository.findByIdAndType(orderPalletRequest.getStatusId(), getEntityName().getEntity())
                .orElseThrow(() ->
                    new EntityNotFoundException(Utility.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND))            
            )
        );

        return modifyAndSave(orderPallet, orderPalletRequest);
    }

    @Override 
    @CacheEvict(value = "orderPallets", key = "#id")
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    protected JpaRepository<OrderPallet, Long> getRepository() {
        return orderPalletRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.ORDER_PALLET;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInPaperCard = paperCardRepository.existsByOrderPalletId(id);
        boolean activeInPickedProduct = pickedProductRepository.existsByOrderPalletId(id);
        return activeInPaperCard || activeInPickedProduct;
    }

    private OrderPallet modifyAndSave(OrderPallet target, OrderPalletRequest from) {
        orderPalletRequestMapper.convertFromRequest(from, target);
        return orderPalletRepository.save(target);
    }
}
