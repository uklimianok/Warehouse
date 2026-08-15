package com.warehouse.demo.controller.warehouseFloor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.demo.dto.employee.organization.FullOrganizationResponse;
import com.warehouse.demo.dto.employee.organization.OrganizationResponse;
import com.warehouse.demo.dto.employee.organization.organizationType.OrganizationTypeResponse;
import com.warehouse.demo.dto.employee.shift.ShiftResponse;
import com.warehouse.demo.dto.order.FullOrderResponse;
import com.warehouse.demo.dto.order.OrderRequest;
import com.warehouse.demo.dto.order.OrderResponse;
import com.warehouse.demo.dto.service.status.StatusResponse;
import com.warehouse.demo.dto.workplace.gate.GateResponse;
import com.warehouse.demo.entity.order.Order;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.order.OrderService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    private static final String READ_ACCESS_ROLES =
        "hasAnyRole('GOODS_PICKER', 'SET_GOODS_EXPORTER', 'COORDINATOR', " + 
        "'DATA_CONTROLLER', 'SHIFT_SUPERVISOR', 'DIRECTOR', " +
        "'ORDERS_PROCEEDER', 'STATISTICS_PROCEEDER', 'DEVELOPER', " +
        "'SYSTEM_ADMINISTRATOR')";
    private static final String READ_UPDATE_ACCESS_ROLES =
        "hasAnyRole('GOODS_PICKER', 'SET_GOODS_EXPORTER', 'COORDINATOR', " +
        "'DATA_CONTROLLER', 'ORDERS_PROCEEDER', 'SYSTEM_ADMINISTRATOR')";
    private static final String CREATE_READ_UPDATE_ACCESS_ROLES = 
        "hasAnyRole('ORDERS_PROCEEDER', 'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES =
        "hasAnyRole('SYSTEM_ADMINISTRATOR')";

    private static final String[] FULL_RESPONSE_ROLES_ARR = 
    {
        "COORDINATOR", "DATA_CONTROLLER", "SHIFT_SUPERVISOR", "DIRECTOR",
        "ORDERS_PROCEEDER", "STATISTICS_PROCEEDER", "DEVELOPER", 
        "SYSTEM_ADMINISTRATOR"
    };

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends OrderResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Order> orders = orderService.readAll();
        List<OrderResponse> ordersResponse = orders
            .stream()
            .map(p -> returnObjectResponse(p, userPrincipal))
            .toList();

        ResponseEntity<List<? extends OrderResponse>> response = new ResponseEntity<>(ordersResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<OrderResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        Order order = orderService.read(id);
        OrderResponse orderResponse = returnObjectResponse(order, userPrincipal);

        ResponseEntity<OrderResponse> response = new ResponseEntity<>(orderResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(CREATE_READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<OrderResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody OrderRequest orderRequest) {
        Order order = orderService.create(orderRequest);
        OrderResponse orderResponse = returnObjectResponse(order, userPrincipal);

        ResponseEntity<OrderResponse> response = new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<OrderResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody OrderRequest orderRequest) {
        Order order = orderService.update(id, orderRequest);
        OrderResponse orderResponse = returnObjectResponse(order, userPrincipal);

        ResponseEntity<OrderResponse> response = new ResponseEntity<>(orderResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        orderService.delete(id);
        String message = Utility.getOutputMessage(EntityName.ORDER, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private OrderResponse returnObjectResponse(Order from, UserPrincipal userPrincipal) {
        OrderResponse orderResponse = null;
        if (userPrincipal.hasAnyRole(FULL_RESPONSE_ROLES_ARR))
            orderResponse = new FullOrderResponse(
                from.getId(),
                new FullOrganizationResponse(
                    from.getStore().getId(),
                    from.getStore().getName(),
                    from.getStore().getOrganizationNumber(),
                    new OrganizationTypeResponse(
                        from.getStore().getOrganizationType().getId(),
                        from.getStore().getOrganizationType().getName()
                    ),
                    from.getStore().getAddress(),
                    from.getStore().getPhoneNumber(),
                    from.getStore().getEmail(),
                    from.getStore().getUrl()
                ),
                from.getGate() != null ? new GateResponse(
                    from.getGate().getId(),
                    from.getGate().getSymbol()
                ) : null,
                new ShiftResponse(
                    from.getShift().getId(),
                    from.getShift().getSymbol()
                ),
                new StatusResponse(
                    from.getStatus().getId(),
                    from.getStatus().getName(),
                    from.getStatus().getType()
                ),
                from.getNote()
            );
        else
            orderResponse = new OrderResponse(
                from.getId(),
                new OrganizationResponse(
                    from.getStore().getId(),
                    from.getStore().getName(),
                    from.getStore().getOrganizationNumber()
                ),
                from.getGate() != null ? new GateResponse(
                    from.getGate().getId(),
                    from.getGate().getSymbol()
                ) : null,
                from.getNote()
            );

        return orderResponse;
    }
}