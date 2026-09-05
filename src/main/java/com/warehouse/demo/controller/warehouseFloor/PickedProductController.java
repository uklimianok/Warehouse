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

import com.warehouse.demo.dto.order.pickedProduct.PickedProductRequest;
import com.warehouse.demo.dto.order.pickedProduct.PickedProductResponse;
import com.warehouse.demo.entity.order.PickedProduct;
import com.warehouse.demo.mapper.order.pickedProduct.PickedProductResponseMapper;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.order.PickedProductService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/picked-products")
@RequiredArgsConstructor
public class PickedProductController {
    private final PickedProductService pickedProductService;
    private final PickedProductResponseMapper pickedProductResponseMapper;

    private static final String READ_ACCESS_ROLES =
        "hasAnyRole('GOODS_PICKER', 'COORDINATOR', 'DATA_CONTROLLER', " + 
        "'SHIFT_SUPERVISOR', 'DIRECTOR', 'STATISTICS_PROCEEDER', " + 
        "'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String READ_UPDATE_ACCESS_ROLES = 
        "hasAnyRole('GOODS_PICKER', 'COORDINATOR', " +
        "'DATA_CONTROLLER', 'SYSTEM_ADMINISTRATOR')";
    private static final String CREATE_READ_UPDATE_ACCESS_ROLES =
        "hasAnyRole('GOODS_PICKER', 'COORDINATOR', " +
        "'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES =
        "hasAnyRole('SYSTEM_ADMINISTRATOR')";

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends PickedProductResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<PickedProduct> pickedProduct = pickedProductService.readAll();
        List<? extends PickedProductResponse> pickedProductResponse = pickedProduct
            .stream()
            .map(pp -> returnObjectResponse(pp, userPrincipal))
            .toList();

        ResponseEntity<List<? extends PickedProductResponse>> response = new ResponseEntity<>(pickedProductResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends PickedProductResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        PickedProduct pickedProduct = pickedProductService.read(id);
        PickedProductResponse pickedProductResponse = returnObjectResponse(pickedProduct, userPrincipal);

        ResponseEntity<? extends PickedProductResponse> response = new ResponseEntity<>(pickedProductResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(CREATE_READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<? extends PickedProductResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody PickedProductRequest pickedProductRequest) {
        PickedProduct pickedProduct = pickedProductService.create(pickedProductRequest);
        PickedProductResponse pickedProductResponse = returnObjectResponse(pickedProduct, userPrincipal);

        ResponseEntity<? extends PickedProductResponse> response = new ResponseEntity<>(pickedProductResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<? extends PickedProductResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody PickedProductRequest pickedProductRequest) {
        PickedProduct pickedProduct = pickedProductService.update(id, pickedProductRequest);
        PickedProductResponse pickedProductResponse = returnObjectResponse(pickedProduct, userPrincipal);

        ResponseEntity<? extends PickedProductResponse> response = new ResponseEntity<>(pickedProductResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        pickedProductService.delete(id);
        String message = Utility.getOutputMessage(EntityName.PICKED_PRODUCT, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private PickedProductResponse returnObjectResponse(PickedProduct from, UserPrincipal principal) {
        PickedProductResponse response = pickedProductResponseMapper.convertToResponse(from);
        return response;
    }
}
