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
import com.warehouse.demo.dto.order.pickedProduct.PickedProductRequest;
import com.warehouse.demo.dto.order.pickedProduct.PickedProductResponse;
import com.warehouse.demo.entity.order.PickedProduct;
import com.warehouse.demo.mapper.order.pickedProduct.PickedProductResponseMapper;
import com.warehouse.demo.service.order.PickedProductService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/picked-products")
@RequiredArgsConstructor
public class PickedProductController {
    private final PickedProductService pickedProductService;
    private final PickedProductResponseMapper pickedProductResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends PickedProductResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<PickedProduct> pickedProduct = pickedProductService.readAll();
        List<? extends PickedProductResponse> pickedProductResponse = pickedProduct
            .stream()
            .map(pp -> returnObjectResponse(pp, userPrincipal))
            .toList();

        return new ResponseEntity<>(pickedProductResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends PickedProductResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        PickedProduct pickedProduct = pickedProductService.read(id);
        PickedProductResponse pickedProductResponse = returnObjectResponse(pickedProduct, userPrincipal);

        return new ResponseEntity<>(pickedProductResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends PickedProductResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid PickedProductRequest pickedProductRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');

        PickedProduct pickedProduct = pickedProductService.create(pickedProductRequest);
        PickedProductResponse pickedProductResponse = returnObjectResponse(pickedProduct, userPrincipal);

        return new ResponseEntity<>(pickedProductResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends PickedProductResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid PickedProductRequest pickedProductRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');

        PickedProduct pickedProduct = pickedProductService.update(id, pickedProductRequest, userPrincipal.getEmployeeNumber());
        PickedProductResponse pickedProductResponse = returnObjectResponse(pickedProduct, userPrincipal);

        return new ResponseEntity<>(pickedProductResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        pickedProductService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.PICKED_PRODUCT, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private PickedProductResponse returnObjectResponse(
        PickedProduct from, 
        UserPrincipal principal
    ) {
        PickedProductResponse response = pickedProductResponseMapper.convertToResponse(from);
        return response;
    }
}
