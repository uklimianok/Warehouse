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
import com.warehouse.demo.dto.order.orderedProduct.OrderedProductRequest;
import com.warehouse.demo.dto.order.orderedProduct.OrderPalletResponse;
import com.warehouse.demo.dto.product.ProductResponse;
import com.warehouse.demo.dto.product.productPackage.ProductPackageResponse;
import com.warehouse.demo.dto.workplace.gate.GateResponse;
import com.warehouse.demo.entity.order.OrderedProduct;
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
    public ResponseEntity<List<? extends OrderPalletResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<OrderedProduct> orderedProducts = orderedProductService.readAll();
        List<? extends OrderPalletResponse> orderedProductsResponse = orderedProducts
            .stream()
            .map(op -> returnObjectResponse(op, userPrincipal))
            .toList();
        
        ResponseEntity<List<? extends OrderPalletResponse>> response = new ResponseEntity<>(orderedProductsResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends OrderPalletResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        OrderedProduct orderedProduct = orderedProductService.read(id);
        OrderPalletResponse orderedProductResponse = returnObjectResponse(orderedProduct, userPrincipal);

        ResponseEntity<? extends OrderPalletResponse> response = new ResponseEntity<>(orderedProductResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(CREATE_READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<? extends OrderPalletResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody OrderedProductRequest orderedProductRequest) {
        OrderedProduct orderedProduct = orderedProductService.create(orderedProductRequest);
        OrderPalletResponse orderedProductResponse = returnObjectResponse(orderedProduct, userPrincipal);

        ResponseEntity<? extends OrderPalletResponse> response = new ResponseEntity<>(orderedProductResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<? extends OrderPalletResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody OrderedProductRequest orderedProductRequest) {
        OrderedProduct orderedProduct = orderedProductService.update(id, orderedProductRequest);
        OrderPalletResponse orderedProductResponse = returnObjectResponse(orderedProduct, userPrincipal);

        ResponseEntity<? extends OrderPalletResponse> response = new ResponseEntity<>(orderedProductResponse, HttpStatus.OK);
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

    private OrderPalletResponse returnObjectResponse(OrderedProduct from, UserPrincipal userPrincipal) {
        OrderPalletResponse orderedProductResponse = new OrderPalletResponse(
            from.getId(),
            new OrderResponse(
                from.getOrder().getId(),
                new OrganizationResponse(
                    from.getOrder().getStore().getId(),
                    from.getOrder().getStore().getName(),
                    from.getOrder().getStore().getOrganizationNumber()
                ),
                from.getOrder() != null ? new GateResponse(
                    from.getOrder().getGate().getId(),
                    from.getOrder().getGate().getSymbol()
                ) : null,
                from.getOrder().getNote()
            ),
            new ProductPackageResponse(
                from.getProductPackage().getId(),
                new ProductResponse(
                    from.getProductPackage().getProduct().getId(),
                    from.getProductPackage().getProduct().getName()
                ),
                from.getProductPackage().getProductsAmount(),
                from.getProductPackage().getVolume(),
                from.getProductPackage().getWeight()
            ),
            from.getOrderedVolume()
        );

        return orderedProductResponse;
    }
}
