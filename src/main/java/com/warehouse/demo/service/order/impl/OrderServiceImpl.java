package com.warehouse.demo.service.order.impl;

import com.warehouse.demo.util.action.PositionInheritanceTree;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.order.OrderRequest;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.entity.order.Order;
import com.warehouse.demo.entity.service.Status;
import com.warehouse.demo.mapper.order.OrderRequestMapper;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.order.OrderPalletRepository;
import com.warehouse.demo.repository.order.OrderRepository;
import com.warehouse.demo.repository.order.OrderedProductRepository;
import com.warehouse.demo.repository.order.ReturnProductRepository;
import com.warehouse.demo.repository.service.StatusRepository;
import com.warehouse.demo.repository.workplace.GateRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.order.OrderService;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.DepartmentCodeNames;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;
import com.warehouse.demo.util.info.StatusInfo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl extends AbstractService<Order, Long> implements OrderService {
    private final PositionInheritanceTree positionInheritanceTree;

    private final OrderService self;

    private final OrderRepository orderRepository;
    private final OrderedProductRepository orderedProductRepository;
    private final OrderPalletRepository orderPalletRepository;
    private final ReturnProductRepository returnProductRepository;
    private final GateRepository gateRepository;
    private final StatusRepository statusRepository;
    private final EmployeeRepository employeeRepository;

    private final OrderRequestMapper orderRequestMapper;

    public static final String GATE_REQUIRED = "must contain any gate.";

    public OrderServiceImpl(
        @Lazy OrderService self, 
        OrderRepository orderRepository, 
        OrderedProductRepository orderedProductRepository, 
        OrderPalletRepository orderPalletRepository, 
        ReturnProductRepository returnProductRepository, 
        GateRepository gateRepository, 
        StatusRepository statusRepository, 
        OrderRequestMapper orderRequestMapper,
        EmployeeRepository employeeRepository, PositionInheritanceTree positionInheritanceTree
    ) {
        this.self = self;   // @Lazy sets only when it is used, not when declared
        this.orderRepository = orderRepository;
        this.orderedProductRepository = orderedProductRepository;
        this.orderPalletRepository = orderPalletRepository;
        this.returnProductRepository = returnProductRepository;
        this.gateRepository = gateRepository;
        this.statusRepository = statusRepository;
        this.orderRequestMapper = orderRequestMapper;
        this.employeeRepository = employeeRepository;
        this.positionInheritanceTree = positionInheritanceTree;
    }

    @Override 
    @Cacheable(value = "orders", key = "#id")
    public Order read(Long id) {
        return super.read(id);
    }

    @Override
    @Transactional
    public Order create(OrderRequest orderRequest) {
        Order order = new Order();
        order.setStatus(statusRepository
            .findByNameAndType(StatusInfo.ORDER_ACCEPTED, Entity.ORDER.getEntity())
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)))  
        );

        return modifyAndSave(order, orderRequest);
    }

    @Override
    @Transactional
    @CacheEvict(value = "orders", key = "#id")
    public Order update(long id, OrderRequest orderRequest, String employeeNumber) {    // Develop status system
        Order order = self.read(id);    // Cached object is provided through proxy "self", not through direct "this"
        Employee employee = employeeRepository.findByEmployeeNumber(employeeNumber)
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.EMPLOYEE, OutputMessage.NOT_FOUND)));

        throwIfNotConfigurable(order, orderRequest, employee);
        configureStatus(order, orderRequest, employee);
        

        if (orderRequest.getGateId() != null)
            order.setGate(gateRepository.findById(orderRequest.getGateId())
                .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.GATE, OutputMessage.NOT_FOUND)))
            );
        else
            order.setGate(null);

        if (!order.getStatus().getName().equals(StatusInfo.ORDER_ACCEPTED) && order.getGate() == null)
            throw new DataIntegrityViolationException(MessageHandler.getOutputMessage(getEntityName(), GATE_REQUIRED));

        Employee callerEmployee = employeeRepository.findByEmployeeNumber(employeeNumber)
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.EMPLOYEE, OutputMessage.NOT_FOUND)));
        if (callerEmployee.getPosition().getDepartment().getCodeName().equals(DepartmentCodeNames.WAREHOUSE_EMPLOYEES_DEPARTMENT))
            orderRequestMapper.convertFromWarehouseEmployeeRequest(orderRequest, order);
        else
            orderRequestMapper.convertFromRequest(orderRequest, order);

        return orderRepository.save(order);
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

    private void throwIfNotConfigurable(Order target, OrderRequest from, Employee subject) {
        Position subjectPosition = subject.getPosition();
        if (
            positionInheritanceTree.isOneOrDescendant(subjectPosition, "ORDERS_PROCEEDER")
            && !target.getStatus().getName().equals(StatusInfo.ORDER_ACCEPTED)  // ORDERS_PROCEEDER can only configure orders with status "Accepted"
        ) throw new DataIntegrityViolationException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));

        Status status = statusRepository.findByIdAndType(from.getStatusId(), Entity.ORDER.getEntity())
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)));
        if (    // Empty gate is not allowed for orders with status "Sent"
            from.getGateId() == null
            && status.getName().equals(StatusInfo.ORDER_SENT)
        ) throw new DataIntegrityViolationException(MessageHandler.getOutputMessage(getEntityName(), GATE_REQUIRED));
    }

    private void configureStatus(Order target, OrderRequest from, Employee subject) {
        Position subjectPosition = subject.getPosition();
        Status newStatus = statusRepository.findByIdAndType(from.getStatusId(), Entity.ORDER.getEntity())
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.STATUS, OutputMessage.NOT_FOUND)));

        switch (newStatus.getName()) {
            case StatusInfo.ORDER_ACCEPTED: {
                if (
                    (positionInheritanceTree.isOneOrDescendant(subjectPosition, "SYSTEM_ADMINISTRATOR")
                    || subjectPosition.getDepartment().getCodeName().equals(DepartmentCodeNames.AUXILIARY_EMPLOYEES_DEPARTMENT)
                    || positionInheritanceTree.isOneOrDescendant(subjectPosition, "ORDERS_PROCEEDER"))
                    && !orderPalletRepository.existsByOrderId(target.getId())
                ) // Status "Accepted" can be set by certain roles, but only if there are no pallets assigned to the order
                    target.setStatus(newStatus);
                else
                    throw new DataIntegrityViolationException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
            } break;
            
            case StatusInfo.ORDER_STARTED, StatusInfo.ORDER_INCOMPLETE, StatusInfo.ORDER_COMPLETE, StatusInfo.ORDER_SENT: { // Statuses are set either automatically or by SYSTEM_ADMINISTRATOR
                if (positionInheritanceTree.isOneOrDescendant(subjectPosition, "SYSTEM_ADMINISTRATOR")) 
                    target.setStatus(newStatus);
                else
                    throw new DataIntegrityViolationException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));
            } break;

            default:
                throw new DataIntegrityViolationException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED)); 
        }
    }
}
