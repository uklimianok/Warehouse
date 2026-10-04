package com.warehouse.demo.controller.http.warehouseFloor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.order.OrderRequest;
import com.warehouse.demo.dto.order.OrderResponse;
import com.warehouse.demo.entity.order.Order;
import com.warehouse.demo.mapper.order.OrderResponseMapper;
import com.warehouse.demo.service.order.OrderService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderResponseMapper orderResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends OrderResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<Order> orders = orderService.readAll();
        List<OrderResponse> ordersResponse = orders
            .stream()
            .map(p -> returnObjectResponse(p, userPrincipal))
            .toList();

        return new ResponseEntity<>(ordersResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends OrderResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');
        
        Order order = orderService.read(id);
        OrderResponse orderResponse = returnObjectResponse(order, userPrincipal);

        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends OrderResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid OrderRequest orderRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');
        
        Order order = orderService.create(orderRequest);
        OrderResponse orderResponse = returnObjectResponse(order, userPrincipal);

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends OrderResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid OrderRequest orderRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');

        Order order = orderService.update(id, orderRequest, userPrincipal.getEmployeeNumber());
        OrderResponse orderResponse = returnObjectResponse(order, userPrincipal);

        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        orderService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.ORDER, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private OrderResponse returnObjectResponse(
        Order from, 
        UserPrincipal principal
    ) {
        String responseType = controllerSecurity.getResponseObjectType(getClass(), principal.getEmployeeNumber());
        return switch (responseType) {
            case "Full" -> orderResponseMapper.convertToFullResponse(from);
            default -> orderResponseMapper.convertToResponse(from);
        };
    }
}