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
import com.warehouse.demo.dto.item.pallet.FullPalletResponse;
import com.warehouse.demo.dto.item.pallet.PalletResponse;
import com.warehouse.demo.dto.order.FullOrderResponse;
import com.warehouse.demo.dto.order.OrderResponse;
import com.warehouse.demo.dto.order.orderPallet.FullOrderPalletResponse;
import com.warehouse.demo.dto.order.orderPallet.OrderPalletRequest;
import com.warehouse.demo.dto.order.orderPallet.OrderPalletResponse;
import com.warehouse.demo.dto.service.status.StatusResponse;
import com.warehouse.demo.dto.workplace.gate.GateResponse;
import com.warehouse.demo.entity.order.OrderPallet;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.order.OrderPalletService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order-pallets")
@RequiredArgsConstructor
public class OrderPalletController {
    private final OrderPalletService orderPalletService;

    private static final String CREATE_ACCESS_ROLES = 
        "hasAnyRole('GOODS_PICKER', 'COORDINATOR', 'SYSTEM_ADMINISTRATOR')";
    private static final String READ_ACCESS_ROLES =
        "hasAnyRole('SET_GOODS_EXPORTER', 'SET_GOODS_LOADER', " +
        "'COORDINATOR', 'DATA_CONTROLLER', 'SHIFT_SUPERVISOR', 'DIRECTOR', " +
        "'STATISTICS_PROCEEDER', 'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String READ_UPDATE_ACCESS_ROLES =
        "hasAnyRole('COORDINATOR', 'DATA_CONTROLLER', 'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES =
        "hasAnyRole('SYSTEM_ADMINISTRATOR')";

    private static final String[] FULL_RESPONSE_ROLES_ARR = 
    {
        "COORDINATOR", "DATA_CONTROLLER", "SHIFT_SUPERVISOR", 
        "DIRECTOR", "STATISTICS_PROCEEDER", "DEVELOPER", "SYSTEM_ADMINISTRATOR"
    };

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends OrderPalletResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<OrderPallet> orderPallets = orderPalletService.readAll();
        List<? extends OrderPalletResponse> orderPalletResponse = orderPallets
            .stream()
            .map(op -> returnObjectResponse(op, userPrincipal))
            .toList();
        
        ResponseEntity<List<? extends OrderPalletResponse>> response = new ResponseEntity<>(orderPalletResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends OrderPalletResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        OrderPallet orderPallet = orderPalletService.read(id);
        OrderPalletResponse orderPalletResponse = returnObjectResponse(orderPallet, userPrincipal);

        ResponseEntity<? extends OrderPalletResponse> response = new ResponseEntity<>(orderPalletResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(CREATE_ACCESS_ROLES)
    public ResponseEntity<? extends OrderPalletResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody OrderPalletRequest orderPalletRequest) {
        OrderPallet orderPallet = orderPalletService.create(orderPalletRequest);
        OrderPalletResponse orderPalletResponse = returnObjectResponse(orderPallet, userPrincipal);

        ResponseEntity<? extends OrderPalletResponse> response = new ResponseEntity<>(orderPalletResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<? extends OrderPalletResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody OrderPalletRequest orderPalletRequest) {
        OrderPallet orderPallet = orderPalletService.update(id, orderPalletRequest);
        OrderPalletResponse orderPalletResponse = returnObjectResponse(orderPallet, userPrincipal);

        ResponseEntity<? extends OrderPalletResponse> response = new ResponseEntity<>(orderPalletResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        orderPalletService.delete(id);
        String message = Utility.getOutputMessage(EntityName.ORDER_PALLET, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private OrderPalletResponse returnObjectResponse(OrderPallet from, UserPrincipal userPrincipal) {
        OrderPalletResponse orderPalletResponse = null;
        if (userPrincipal.hasAnyRole(FULL_RESPONSE_ROLES_ARR))
            orderPalletResponse = new FullOrderPalletResponse(
                from.getId(),
                new FullOrderResponse(
                    from.getOrder().getId(),
                    new FullOrganizationResponse(
                        from.getOrder().getStore().getId(),
                        from.getOrder().getStore().getName(),
                        from.getOrder().getStore().getOrganizationNumber(),
                        new OrganizationTypeResponse(
                            from.getOrder().getStore().getOrganizationType().getId(),
                            from.getOrder().getStore().getOrganizationType().getName()
                        ),
                        from.getOrder().getStore().getAddress(),
                        from.getOrder().getStore().getPhoneNumber(),
                        from.getOrder().getStore().getEmail(),
                        from.getOrder().getStore().getUrl()
                    ),
                    from.getOrder().getGate() != null ? new GateResponse(
                        from.getOrder().getGate().getId(),
                        from.getOrder().getGate().getSymbol()
                    ) : null,
                    new ShiftResponse(
                        from.getOrder().getShift().getId(),
                        from.getOrder().getShift().getSymbol()
                    ),
                    new StatusResponse(
                        from.getOrder().getStatus().getId(),
                        from.getOrder().getStatus().getName(),
                        from.getOrder().getStatus().getType()
                    ),
                    from.getOrder().getNote()
                ),
                new FullPalletResponse(
                    from.getPallet().getId(),
                    from.getPallet().getName(),
                    from.getPallet().getColor(),
                    from.getPallet().getLength(),
                    from.getPallet().getWidth(),
                    from.getPallet().getHeight(),
                    from.getPallet().getWeight()
                ),
                new StatusResponse(
                    from.getStatus().getId(),
                    from.getStatus().getName(),
                    from.getStatus().getType()
                )
            );
        else
            orderPalletResponse = new OrderPalletResponse(
                from.getId(),
                new OrderResponse(
                    from.getOrder().getId(),
                    new OrganizationResponse(
                        from.getOrder().getStore().getId(),
                        from.getOrder().getStore().getName(),
                        from.getOrder().getStore().getOrganizationNumber()
                    ),
                    from.getOrder().getGate() != null ? new GateResponse(
                        from.getOrder().getGate().getId(),
                        from.getOrder().getGate().getSymbol()
                    ) : null,
                    from.getOrder().getNote()
                ),
                new PalletResponse(
                    from.getPallet().getId(),
                    from.getPallet().getName(),
                    from.getPallet().getColor()
                )
            );

        return orderPalletResponse;
    }
}
