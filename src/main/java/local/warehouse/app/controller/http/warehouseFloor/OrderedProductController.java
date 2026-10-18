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
import local.warehouse.app.dto.order.orderedProduct.OrderedProductRequest;
import local.warehouse.app.dto.order.orderedProduct.OrderedProductResponse;
import local.warehouse.app.entity.order.OrderedProduct;
import local.warehouse.app.mapper.order.orderedProduct.OrderedProductResponseMapper;
import local.warehouse.app.service.order.OrderedProductService;
import local.warehouse.app.util.action.ControllerSecurity;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ordered-products")
@RequiredArgsConstructor
public class OrderedProductController {
    private final OrderedProductService orderedProductService;
    private final OrderedProductResponseMapper orderedProductResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends OrderedProductResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<OrderedProduct> orderedProducts = orderedProductService.readAll();
        List<? extends OrderedProductResponse> orderedProductsResponse = orderedProducts
            .stream()
            .map(op -> returnObjectResponse(op, userPrincipal))
            .toList();
        
        return new ResponseEntity<>(orderedProductsResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends OrderedProductResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        OrderedProduct orderedProduct = orderedProductService.read(id);
        OrderedProductResponse orderedProductResponse = returnObjectResponse(orderedProduct, userPrincipal);

        return new ResponseEntity<>(orderedProductResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends OrderedProductResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid OrderedProductRequest orderedProductRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');

        OrderedProduct orderedProduct = orderedProductService.create(orderedProductRequest);
        OrderedProductResponse orderedProductResponse = returnObjectResponse(orderedProduct, userPrincipal);

        return new ResponseEntity<>(orderedProductResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends OrderedProductResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody OrderedProductRequest orderedProductRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');

        OrderedProduct orderedProduct = orderedProductService.update(id, orderedProductRequest);
        OrderedProductResponse orderedProductResponse = returnObjectResponse(orderedProduct, userPrincipal);

        return new ResponseEntity<>(orderedProductResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        orderedProductService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.ORDERED_PRODUCT, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private OrderedProductResponse returnObjectResponse(
        OrderedProduct from, 
        UserPrincipal principal
    ) {
        OrderedProductResponse response = orderedProductResponseMapper.convertToResponse(from);
        return response;
    }
}
