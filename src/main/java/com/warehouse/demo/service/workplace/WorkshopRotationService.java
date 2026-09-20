package com.warehouse.demo.service.workplace;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.entity.workplace.Workshop;
import com.warehouse.demo.event.product.ProductPalletEvent;
import com.warehouse.demo.mapper.product.productPallet.ProductPalletResponseMapper;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.product.ProductPalletRepository;
import com.warehouse.demo.repository.workplace.WorkshopRepository;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.persistence.EntityNotFoundException;

@Service 
public class WorkshopRotationService {
    private final WorkshopRotationService self;

    private final ProductPalletRepository productPalletRepository;
    private final EmployeeRepository employeeRepository;
    private final WorkshopRepository workshopRepository;

    private final ProductPalletResponseMapper productPalletResponseMapper;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final int MAX_ATTEMPTS = 10;

    public WorkshopRotationService(
        @Lazy WorkshopRotationService self,
        ProductPalletRepository productPalletRepository,
        EmployeeRepository employeeRepository,
        WorkshopRepository workshopRepository,
        ProductPalletResponseMapper productPalletResponseMapper,
        SimpMessagingTemplate simpMessagingTemplate
    ) {
        this.self = self;
        this.productPalletRepository = productPalletRepository;
        this.employeeRepository = employeeRepository;
        this.workshopRepository = workshopRepository;
        this.productPalletResponseMapper = productPalletResponseMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @KafkaListener(groupId = "1", topics = "product-pallet-unloaded-status-next-work-station-not-null-event")
    @Transactional 
    public void consumeEvent(ProductPalletEvent productPalletEvent) {
        ProductPallet productPallet = productPalletRepository.findByPalletNumber(productPalletEvent.getPalletNumber())
            .orElseThrow(() -> new DataIntegrityViolationException(Utility.getOutputMessage(Entity.PRODUCT_PALLET, OutputMessage.NOT_FOUND)));
        Workshop targetWorkshop = productPallet.getNextWorkStation().getWorkshop();
        List<Employee> operators = employeeRepository.findAllByPositionCodeNameAndWorkshopId("OPERATOR", productPallet.getNextWorkStation().getWorkshop().getId())
            .stream()
            .sorted((e1, e2) -> Long.compare(e1.getId(), e2.getId()))
            .toList();

        if (operators.isEmpty()) return;

        int attempts = 0;
        while (true) {
            try {   // Version conflict
                Employee targetOperator = self.assignNextOperator(targetWorkshop.getId(), operators);
        
                simpMessagingTemplate.convertAndSendToUser(
                    targetOperator.getEmployeeNumber(), 
                    "/queue/notify", 
                    productPalletResponseMapper.convertToTransferResponse(productPallet)
                ); 

                return;
            } catch (ObjectOptimisticLockingFailureException e) {
                if (++attempts >= MAX_ATTEMPTS)
                    throw new DataIntegrityViolationException(Utility.getOutputMessage(OutputMessage.OPERATION_FAILED));
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)  // Start a brand-new independent transaction
    public Employee assignNextOperator(long workshopId, List<Employee> operators) {
        Workshop workshop = workshopRepository.findById(workshopId)
            .orElseThrow(() -> new EntityNotFoundException(Utility.getOutputMessage(Entity.WORKSHOP, OutputMessage.NOT_FOUND)));    // Call the same Workshop object to detect version conflict
        if (workshop.getLastAssignedOperatorIndex() >= operators.size())
            workshop.setLastAssignedOperatorIndex(0);

        Employee pickedOperator = operators.get(workshop.getLastAssignedOperatorIndex());
        
        workshop.setLastAssignedOperatorIndex(workshop.getLastAssignedOperatorIndex() + 1); // Hibernate automatically does save() because of the transaction lifetime

        return pickedOperator;
    }
}
