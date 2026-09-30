package com.warehouse.demo.controller.http.warehouseFloor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.order.orderPallet.OrderPalletRequest;
import com.warehouse.demo.dto.order.orderPallet.OrderPalletResponse;
import com.warehouse.demo.entity.order.OrderPallet;
import com.warehouse.demo.mapper.order.orderPallet.OrderPalletResponseMapper;
import com.warehouse.demo.service.order.OrderPalletService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order-pallets")
@RequiredArgsConstructor
public class OrderPalletController {
    private final OrderPalletService orderPalletService;
    private final OrderPalletResponseMapper orderPalletResponseMapper;

    private final ControllerSecurity controllerSecurity;

    private static final String[] FULL_RESPONSE_ROLES_ARR = 
    {
        "COORDINATOR", "DATA_CONTROLLER", "SHIFT_SUPERVISOR", 
        "DIRECTOR", "STATISTICS_PROCEEDER", "DEVELOPER", "SYSTEM_ADMINISTRATOR"
    };

    @GetMapping
    public ResponseEntity<List<? extends OrderPalletResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        List<OrderPallet> orderPallets = orderPalletService.readAll();
        List<? extends OrderPalletResponse> orderPalletResponse = orderPallets
            .stream()
            .map(op -> returnObjectResponse(op, userPrincipal))
            .toList();
        
        return new ResponseEntity<>(orderPalletResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends OrderPalletResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        OrderPallet orderPallet = orderPalletService.read(id);
        OrderPalletResponse orderPalletResponse = returnObjectResponse(orderPallet, userPrincipal);

        return new ResponseEntity<>(orderPalletResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends OrderPalletResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody OrderPalletRequest orderPalletRequest) {
        throwIfUnauthorized('C', userPrincipal.getMainRole());

        OrderPallet orderPallet = orderPalletService.create(orderPalletRequest);
        OrderPalletResponse orderPalletResponse = returnObjectResponse(orderPallet, userPrincipal);

        return new ResponseEntity<>(orderPalletResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends OrderPalletResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody OrderPalletRequest orderPalletRequest) {
        throwIfUnauthorized('U', userPrincipal.getMainRole());

        OrderPallet orderPallet = orderPalletService.update(id, orderPalletRequest);
        OrderPalletResponse orderPalletResponse = returnObjectResponse(orderPallet, userPrincipal);

        return new ResponseEntity<>(orderPalletResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('D', userPrincipal.getMainRole());

        orderPalletService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.ORDER_PALLET, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private OrderPalletResponse returnObjectResponse(OrderPallet from, UserPrincipal principal) {
        OrderPalletResponse response = null;
        if (principal.hasAnyRole(FULL_RESPONSE_ROLES_ARR))
            response = orderPalletResponseMapper.convertToFullResponse(from);
        else
            response = orderPalletResponseMapper.convertToResponse(from);
        
        return response;
    }

    private void throwIfUnauthorized(char mode, String role) {
        if (!controllerSecurity.getAccessRoles(getClass(), mode).contains(role))
            throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));
    }
}
