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
import com.warehouse.demo.dto.order.orderedProduct.OrderedProductRequest;
import com.warehouse.demo.dto.order.orderedProduct.OrderedProductResponse;
import com.warehouse.demo.entity.order.OrderedProduct;
import com.warehouse.demo.mapper.order.orderedProduct.OrderedProductResponseMapper;
import com.warehouse.demo.service.order.OrderedProductService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ordered-products")
@RequiredArgsConstructor
public class OrderedProductController {
    private final OrderedProductService orderedProductService;
    private final OrderedProductResponseMapper orderedProductResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends OrderedProductResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        List<OrderedProduct> orderedProducts = orderedProductService.readAll();
        List<? extends OrderedProductResponse> orderedProductsResponse = orderedProducts
            .stream()
            .map(op -> returnObjectResponse(op, userPrincipal))
            .toList();
        
        return new ResponseEntity<>(orderedProductsResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends OrderedProductResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        OrderedProduct orderedProduct = orderedProductService.read(id);
        OrderedProductResponse orderedProductResponse = returnObjectResponse(orderedProduct, userPrincipal);

        return new ResponseEntity<>(orderedProductResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends OrderedProductResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody OrderedProductRequest orderedProductRequest) {
        throwIfUnauthorized('C', userPrincipal.getMainRole());

        OrderedProduct orderedProduct = orderedProductService.create(orderedProductRequest);
        OrderedProductResponse orderedProductResponse = returnObjectResponse(orderedProduct, userPrincipal);

        return new ResponseEntity<>(orderedProductResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends OrderedProductResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody OrderedProductRequest orderedProductRequest) {
        throwIfUnauthorized('U', userPrincipal.getMainRole());

        OrderedProduct orderedProduct = orderedProductService.update(id, orderedProductRequest);
        OrderedProductResponse orderedProductResponse = returnObjectResponse(orderedProduct, userPrincipal);

        return new ResponseEntity<>(orderedProductResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('D', userPrincipal.getMainRole());

        orderedProductService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.ORDERED_PRODUCT, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private OrderedProductResponse returnObjectResponse(OrderedProduct from, UserPrincipal principal) {
        OrderedProductResponse response = orderedProductResponseMapper.convertToResponse(from);
        return response;
    }

    private void throwIfUnauthorized(char mode, String role) {
        if (!controllerSecurity.getAccessRoles(getClass(), mode).contains(role))
            throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));
    }
}
