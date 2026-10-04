package com.warehouse.demo.service.order.impl;

import com.warehouse.demo.repository.service.StatusRepository;

import com.warehouse.demo.util.action.PositionInheritanceTree;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.order.orderPallet.OrderPalletRequest;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.entity.order.OrderPallet;
import com.warehouse.demo.entity.service.Status;
import com.warehouse.demo.mapper.order.orderPallet.OrderPalletRequestMapper;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.item.PaperCardRepository;
import com.warehouse.demo.repository.order.OrderPalletRepository;
import com.warehouse.demo.repository.order.OrderRepository;
import com.warehouse.demo.repository.order.PickedProductRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.order.OrderPalletService;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.DepartmentCodeNames;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;
import com.warehouse.demo.util.info.StatusInfo;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderPalletServiceImpl extends AbstractService<OrderPallet, Long> implements OrderPalletService {
    private final PositionInheritanceTree positionInheritanceTree;

    private final OrderPalletService self;

    private final StatusRepository statusRepository;
    private final OrderPalletRepository orderPalletRepository;
    private final PaperCardRepository paperCardRepository;
    private final PickedProductRepository pickedProductRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;

    private final OrderPalletRequestMapper orderPalletRequestMapper;

    private final CacheManager cacheManager;

    public OrderPalletServiceImpl(
        @Lazy OrderPalletService self, 
        StatusRepository statusRepository, 
        OrderPalletRepository orderPalletRepository, 
        PaperCardRepository paperCardRepository, 
        PickedProductRepository pickedProductRepository, 
        OrderPalletRequestMapper orderPalletRequestMapper,
        EmployeeRepository employeeRepository,
        OrderRepository orderRepository, 
        PositionInheritanceTree positionInheritanceTree,
        CacheManager cacheManager
    ) {
        this.self = self;
        this.statusRepository = statusRepository;
        this.orderPalletRepository = orderPalletRepository;
        this.paperCardRepository = paperCardRepository;
        this.pickedProductRepository = pickedProductRepository;
        this.orderPalletRequestMapper = orderPalletRequestMapper;
        this.employeeRepository = employeeRepository;
        this.orderRepository = orderRepository;
        this.positionInheritanceTree = positionInheritanceTree;
        this.cacheManager = cacheManager;
    }

    @Override 
    @Cacheable(value = "orderPallets", key = "#id")
    public OrderPallet read(Long id) {
        return super.read(id);
    }

    @Override
    @Transactional 
    public OrderPallet create(OrderPalletRequest orderPalletRequest) {
        OrderPallet orderPallet = new OrderPallet();
        orderPallet.setStatus(
            statusRepository.findByNameAndType(StatusInfo.ORDER_PALLET_PICKING, getEntityName().getEntity())
                .orElseThrow(() ->
                    new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)) 
            )   
        );

        OrderPallet savedOrderPallet = modifyAndSave(orderPallet, orderPalletRequest);
        changeOrderStatus(savedOrderPallet.getOrder().getId(), StatusInfo.ORDER_STARTED);

        return savedOrderPallet;
    }

    @Override
    @Transactional
    @CacheEvict(value = "orderPallets", key = "#id")
    public OrderPallet update(long id, OrderPalletRequest orderPalletRequest, String employeeNumber) {  // Develop status system
        OrderPallet orderPallet = self.read(id);
        Employee callerEmployee = employeeRepository.findByEmployeeNumber(employeeNumber)
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.EMPLOYEE, OutputMessage.NOT_FOUND)));

        configureStatus(orderPallet, orderPalletRequest, callerEmployee);

        if (callerEmployee.getPosition().getDepartment().getCodeName().equals(DepartmentCodeNames.WAREHOUSE_EMPLOYEES_DEPARTMENT))
            orderPalletRequestMapper.convertFromWarehouseEmployeeRequest(orderPalletRequest, orderPallet);
        else
            orderPalletRequestMapper.convertFromRequest(orderPalletRequest, orderPallet);

        OrderPallet savedOrderPallet = orderPalletRepository.save(orderPallet);
        
        if (savedOrderPallet.getStatus().getName().equals(StatusInfo.ORDER_PALLET_PICKED)) {
            boolean allPickedProductsCompleted = orderPalletRepository.areAllOrderedProductExistInPickedProduct(savedOrderPallet.getOrder().getId());
            if (allPickedProductsCompleted)
                changeOrderStatus(savedOrderPallet.getOrder().getId(), StatusInfo.ORDER_COMPLETE); 
            else 
                changeOrderStatus(savedOrderPallet.getOrder().getId(), StatusInfo.ORDER_INCOMPLETE);
        } else {
            Status orderPalletSentStatus = statusRepository.findByNameAndType(StatusInfo.ORDER_PALLET_SENT, Entity.ORDER_PALLET.getEntity())
                .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)));

            if (orderPalletRepository.areAllInOrderIdHaveStatusId(savedOrderPallet.getOrder().getId(), orderPalletSentStatus.getId()))
                changeOrderStatus(savedOrderPallet.getOrder().getId(), StatusInfo.ORDER_SENT);
        }

        return savedOrderPallet;
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

    private void configureStatus(OrderPallet target, OrderPalletRequest from, Employee subject) {
        Position subjectPosition = subject.getPosition();
        Status oldStatus = target.getStatus();
        Status newStatus = statusRepository.findByIdAndType(from.getStatusId(), getEntityName().getEntity())
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)));

        switch (newStatus.getName()) {
            case StatusInfo.ORDER_PALLET_PICKING: {
                if (
                    (subjectPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.AUXILIARY_EMPLOYEES_DEPARTMENT)
                    || positionInheritanceTree.isOneOrDescendant(subjectPosition, "SYSTEM_ADMINISTRATOR"))
                    && !pickedProductRepository.existsByOrderPalletId(target.getId())
                )   // Status "Picking" can be set by certain roles, but only if there are no picked products assigned to the order pallet
                    target.setStatus(newStatus);
                else
                    throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
            } break;

            case StatusInfo.ORDER_PALLET_PICKED: {
                if (
                    (oldStatus.getName().equals(StatusInfo.ORDER_PALLET_PICKING)
                    && positionInheritanceTree.isOneOrDescendant(subjectPosition, "GOODS_PICKER"))
                    || (
                        (subjectPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.AUXILIARY_EMPLOYEES_DEPARTMENT)
                        || positionInheritanceTree.isOneOrDescendant(subjectPosition, "SYSTEM_ADMINISTRATOR"))
                        && !pickedProductRepository.existsByOrderPalletIdAndCompleted(target.getId(), false)
                    )
                )   // Status "Picked" can be set by certain roles, but only if all picked products assigned to the order pallet are completed
                    target.setStatus(newStatus);
                else
                    throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
            } break;

            case StatusInfo.ORDER_PALLET_EXPORTING: {
                if (
                    (oldStatus.getName().equals(StatusInfo.ORDER_PALLET_PICKED)
                    && (positionInheritanceTree.isOneOrDescendant(subjectPosition, "SET_GOODS_EXPORTER")))
                    || (
                        (subjectPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.AUXILIARY_EMPLOYEES_DEPARTMENT)
                        || positionInheritanceTree.isOneOrDescendant(subjectPosition, "SYSTEM_ADMINISTRATOR"))
                        && !pickedProductRepository.existsByOrderPalletIdAndCompleted(target.getId(), false)
                    )   
                )   // Status "Exporting" can be set either by "Set Goods Exporter" automatically or as "Picked" status
                    target.setStatus(newStatus);
                else
                    throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
            } break;

            case StatusInfo.ORDER_PALLET_LOADING: {
                if (
                    (oldStatus.getName().equals(StatusInfo.ORDER_PALLET_EXPORTING)
                    && (positionInheritanceTree.isOneOrDescendant(subjectPosition, "SET_GOODS_LOADER")))
                    || (
                        (subjectPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.AUXILIARY_EMPLOYEES_DEPARTMENT)
                        || positionInheritanceTree.isOneOrDescendant(subjectPosition, "SYSTEM_ADMINISTRATOR"))
                        && !pickedProductRepository.existsByOrderPalletIdAndCompleted(target.getId(), false)
                    ) 
                ) 
                    target.setStatus(newStatus);
                else
                    throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
            } break;

            case StatusInfo.ORDER_PALLET_SENT: {
                if (
                    oldStatus.getName().equals(StatusInfo.ORDER_PALLET_LOADING)
                    && (
                        (subjectPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.AUXILIARY_EMPLOYEES_DEPARTMENT)
                        || positionInheritanceTree.isOneOrDescendant(subjectPosition, "SYSTEM_ADMINISTRATOR"))
                        && !pickedProductRepository.existsByOrderPalletIdAndCompleted(target.getId(), false)
                    )   
                )   // Status "Sent" can be set only from "Loading" status
                    target.setStatus(newStatus);
                else
                    throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
            }
        }
    }

    private void changeOrderStatus(long orderId, String statusName) {
        Status orderSentStatus = statusRepository.findByNameAndType(statusName, Entity.ORDER.getEntity())
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)));

        int affectedRows = orderRepository.updateStatusById(orderId, orderSentStatus);
        if (affectedRows < 1) 
            throw new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.ORDER, OutputMessage.NOT_FOUND));

        cacheManager.getCache("orders").evict(orderId);  // Cache is deleted only after DB operation
    }
}
