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

import com.warehouse.demo.dto.order.orderedProduct.OrderedProductRequest;
import com.warehouse.demo.dto.order.orderedProduct.OrderedProductResponse;
import com.warehouse.demo.entity.order.OrderedProduct;
import com.warehouse.demo.mapper.order.orderedProduct.OrderedProductResponseMapper;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.order.OrderedProductService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ordered-products")
@RequiredArgsConstructor
public class OrderedProductController {
    private final OrderedProductService orderedProductService;
    private final OrderedProductResponseMapper orderedProductResponseMapper;

    private static final String READ_ACCESS_ROLES =
        "hasAnyRole('GOODS_PICKER', 'COORDINATOR', " + 
        "'DATA_CONTROLLER', 'SHIFT_SUPERVISOR', 'DIRECTOR', " +
        "'ORDERS_PROCEEDER', 'STATISTICS_PROCEEDER', 'DEVELOPER', " +
        "'SYSTEM_ADMINISTRATOR')";
    private static final String READ_UPDATE_ACCESS_ROLES =
        "hasAnyRole('COORDINATOR', 'DATA_CONTROLLER', 'ORDERS_PROCEEDER', " +
        "'SYSTEM_ADMINISTRATOR')";
    private static final String CREATE_READ_UPDATE_ACCESS_ROLES = 
        "hasAnyRole('ORDERS_PROCEEDER', 'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES =
        "hasAnyRole('SYSTEM_ADMINISTRATOR')";

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends OrderedProductResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<OrderedProduct> orderedProducts = orderedProductService.readAll();
        List<? extends OrderedProductResponse> orderedProductsResponse = orderedProducts
            .stream()
            .map(op -> returnObjectResponse(op, userPrincipal))
            .toList();
        
        ResponseEntity<List<? extends OrderedProductResponse>> response = new ResponseEntity<>(orderedProductsResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends OrderedProductResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        OrderedProduct orderedProduct = orderedProductService.read(id);
        OrderedProductResponse orderedProductResponse = returnObjectResponse(orderedProduct, userPrincipal);

        ResponseEntity<? extends OrderedProductResponse> response = new ResponseEntity<>(orderedProductResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(CREATE_READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<? extends OrderedProductResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody OrderedProductRequest orderedProductRequest) {
        OrderedProduct orderedProduct = orderedProductService.create(orderedProductRequest);
        OrderedProductResponse orderedProductResponse = returnObjectResponse(orderedProduct, userPrincipal);

        ResponseEntity<? extends OrderedProductResponse> response = new ResponseEntity<>(orderedProductResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<? extends OrderedProductResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody OrderedProductRequest orderedProductRequest) {
        OrderedProduct orderedProduct = orderedProductService.update(id, orderedProductRequest);
        OrderedProductResponse orderedProductResponse = returnObjectResponse(orderedProduct, userPrincipal);

        ResponseEntity<? extends OrderedProductResponse> response = new ResponseEntity<>(orderedProductResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        orderedProductService.delete(id);
        String message = Utility.getOutputMessage(EntityName.ORDERED_PRODUCT, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private OrderedProductResponse returnObjectResponse(OrderedProduct from, UserPrincipal principal) {
        OrderedProductResponse response = orderedProductResponseMapper.convertToResponse(from);
        return response;
    }
}
