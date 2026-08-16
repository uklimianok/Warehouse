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

import com.warehouse.demo.dto.employee.organization.OrganizationResponse;
import com.warehouse.demo.dto.order.OrderResponse;
import com.warehouse.demo.dto.order.returnProduct.ReturnProductRequest;
import com.warehouse.demo.dto.order.returnProduct.ReturnProductResponse;
import com.warehouse.demo.dto.product.ProductResponse;
import com.warehouse.demo.dto.workplace.gate.GateResponse;
import com.warehouse.demo.entity.order.ReturnProduct;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.order.ReturnProductService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/return-products")
@RequiredArgsConstructor
public class ReturnProductController {
    private final ReturnProductService returnProductService;

    private static final String READ_ACCESS_ROLES =
        "hasAnyRole('RETURN_GOODS_CONTROLLER', 'COORDINATOR', " +
        "'DATA_CONTROLLER', 'SHIFT_SUPERVISOR', 'DIRECTOR', " +
        "'STATISTICS_PROCEEDER', 'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String READ_UPDATE_ACCESS_ROLES = 
        "hasAnyRole('RETURN_GOODS_CONTROLLER', 'COORDINATOR', " +
        "'DATA_CONTROLLER', 'SYSTEM_ADMINISTRATOR')";
    private static final String CREATE_READ_UPDATE_ACCESS_ROLES =
        "hasAnyRole('RETURN_GOODS_CONTROLLER', 'COORDINATOR', " +
        "'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES =
        "hasAnyRole('SYSTEM_ADMINISTRATOR')";

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends ReturnProductResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<ReturnProduct> returnProducts = returnProductService.readAll();
        List<? extends ReturnProductResponse> returnProductResponse = returnProducts
            .stream()
            .map(rp -> returnObjectResponse(rp, userPrincipal))
            .toList();

        ResponseEntity<List<? extends ReturnProductResponse>> response = new ResponseEntity<>(returnProductResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends ReturnProductResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        ReturnProduct returnProduct = returnProductService.read(id);
        ReturnProductResponse returnProductResponse = returnObjectResponse(returnProduct, userPrincipal);

        ResponseEntity<? extends ReturnProductResponse> response = new ResponseEntity<>(returnProductResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(CREATE_READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<? extends ReturnProductResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ReturnProductRequest returnProductRequest) {
        ReturnProduct returnProduct = returnProductService.create(returnProductRequest);
        ReturnProductResponse returnProductResponse = returnObjectResponse(returnProduct, userPrincipal);

        ResponseEntity<? extends ReturnProductResponse> response = new ResponseEntity<>(returnProductResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<? extends ReturnProductResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody ReturnProductRequest returnProductRequest) {
        ReturnProduct returnProduct = returnProductService.update(id, returnProductRequest);
        ReturnProductResponse returnProductResponse = returnObjectResponse(returnProduct, userPrincipal);

        ResponseEntity<? extends ReturnProductResponse> response = new ResponseEntity<>(returnProductResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        returnProductService.delete(id);
        String message = Utility.getOutputMessage(EntityName.RETURN_PRODUCT, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }
    
    private ReturnProductResponse returnObjectResponse(ReturnProduct from, UserPrincipal userPrincipal) {
        ReturnProductResponse returnProductResponse = new ReturnProductResponse(
            from.getId(),
            new OrderResponse(
                from.getOrder().getId(),
                new OrganizationResponse(
                    from.getOrder().getStore().getId(),
                    from.getOrder().getStore().getName(),
                    from.getOrder().getStore().getOrganizationNumber()
                ),
                from.getOrder().getGate() != null ? new GateResponse(   // null safe
                    from.getOrder().getGate().getId(),
                    from.getOrder().getGate().getSymbol()
                ) : null,
                from.getOrder().getNote()
            ),
            new ProductResponse(
                from.getProduct().getId(),
                from.getProduct().getName()
            ),
            from.getProductsAmount()
        );

        return returnProductResponse;
    }
}