package com.warehouse.demo.service.employee;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.event.product.ProductPalletEvent;
import com.warehouse.demo.mapper.product.productPallet.ProductPalletResponseMapper;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.product.ProductPalletRepository;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class DataControllerNotificationService {
    private final ProductPalletRepository productPalletRepository;
    private final EmployeeRepository employeeRepository;

    private final ProductPalletResponseMapper productPalletResponseMapper;
    
    private final SimpMessagingTemplate simpMessagingTemplate;

    @KafkaListener(groupId = "1", topics = "product-pallet-unloaded-status-next-work-station-null-event")
    @Transactional 
    public void consumeEvent(ProductPalletEvent productPalletEvent) {
        ProductPallet productPallet = productPalletRepository.findByPalletNumber(productPalletEvent.getPalletNumber())
            .orElseThrow(() -> new DataIntegrityViolationException(Utility.getOutputMessage(Entity.PRODUCT_PALLET, OutputMessage.NOT_FOUND)));
        List<Employee> dataControllers = employeeRepository.findAllByPositionCodeName("DATA_CONTROLLER");

        if (dataControllers.isEmpty()) return;

        for (Employee dataController : dataControllers) {
            simpMessagingTemplate.convertAndSendToUser(
                dataController.getEmployeeNumber(), 
                "/queue/notify", 
                productPalletResponseMapper.convertToFullResponse(productPallet)
            );
        }
    }
}
