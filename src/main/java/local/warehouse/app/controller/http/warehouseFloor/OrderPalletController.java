package local.warehouse.app.controller.http.warehouseFloor;

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

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import local.warehouse.app.configuration.security.UserPrincipal;
import local.warehouse.app.dto.order.orderPallet.OrderPalletRequest;
import local.warehouse.app.dto.order.orderPallet.OrderPalletResponse;
import local.warehouse.app.entity.order.OrderPallet;
import local.warehouse.app.mapper.order.orderPallet.OrderPalletResponseMapper;
import local.warehouse.app.service.order.OrderPalletService;
import local.warehouse.app.util.action.ControllerSecurity;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order-pallets")
@RequiredArgsConstructor
public class OrderPalletController {
    private final OrderPalletService orderPalletService;
    private final OrderPalletResponseMapper orderPalletResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends OrderPalletResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<OrderPallet> orderPallets = orderPalletService.readAll();
        List<? extends OrderPalletResponse> orderPalletResponse = orderPallets
            .stream()
            .map(op -> returnObjectResponse(op, userPrincipal))
            .toList();
        
        return new ResponseEntity<>(orderPalletResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends OrderPalletResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        OrderPallet orderPallet = orderPalletService.read(id);
        OrderPalletResponse orderPalletResponse = returnObjectResponse(orderPallet, userPrincipal);

        return new ResponseEntity<>(orderPalletResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends OrderPalletResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid OrderPalletRequest orderPalletRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');

        OrderPallet orderPallet = orderPalletService.create(orderPalletRequest);
        OrderPalletResponse orderPalletResponse = returnObjectResponse(orderPallet, userPrincipal);

        return new ResponseEntity<>(orderPalletResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends OrderPalletResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid OrderPalletRequest orderPalletRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');

        OrderPallet orderPallet = orderPalletService.update(id, orderPalletRequest, userPrincipal.getEmployeeNumber());
        OrderPalletResponse orderPalletResponse = returnObjectResponse(orderPallet, userPrincipal);

        return new ResponseEntity<>(orderPalletResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        orderPalletService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.ORDER_PALLET, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private OrderPalletResponse returnObjectResponse(
        OrderPallet from, 
        UserPrincipal principal
    ) {
        String responseType = controllerSecurity.getResponseObjectType(getClass(), principal.getEmployeeNumber());
        return switch (responseType) {
            case "Full" -> orderPalletResponseMapper.convertToFullResponse(from);
            default -> orderPalletResponseMapper.convertToResponse(from);
        };
    }
}
