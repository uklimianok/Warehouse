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

import com.warehouse.demo.dto.product.ProductRequest;
import com.warehouse.demo.dto.product.ProductResponse;
import com.warehouse.demo.entity.product.Product;
import com.warehouse.demo.mapper.product.ProductResponseMapper;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.product.ProductService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductResponseMapper productMapper;

    private static final String READ_ACCESS_ROLES = 
        "hasAnyRole('GOODS_UNLOADER', 'GOODS_PICKER', 'OPERATOR', " +
        "'RETURN_GOODS_CONTROLLER', 'COORDINATOR', 'DATA_CONTROLLER', " +
        "'DIRECTOR', 'ORDERS_PROCEEDER', 'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES = 
        "hasAnyRole('DATA_CONTROLLER', 'SYSTEM_ADMINISTRATOR')";

    private static final String[] FULL_ACCESS_ROLES_ARR = 
        {"DATA_CONTROLLER", "SYSTEM_ADMINISTRATOR"};

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends ProductResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Product> products = productService.readAll();
        List<ProductResponse> productResponses = products
            .stream()
            .map(p -> returnObjectResponse(p, userPrincipal))
            .toList();

        ResponseEntity<List<? extends ProductResponse>> response = new ResponseEntity<>(productResponses, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends ProductResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        Product product = productService.read(id);
        ProductResponse productResponse = returnObjectResponse(product, userPrincipal);

        ResponseEntity<ProductResponse> response = new ResponseEntity<>(productResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends ProductResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ProductRequest productRequest) {
        Product product = productService.create(productRequest);
        ProductResponse productResponse = returnObjectResponse(product, userPrincipal);

        ResponseEntity<ProductResponse> response = new ResponseEntity<>(productResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends ProductResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody ProductRequest productRequest) {
        Product product = productService.update(id, productRequest);
        ProductResponse productResponse = returnObjectResponse(product, userPrincipal);

        ResponseEntity<ProductResponse> response = new ResponseEntity<>(productResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        productService.delete(id);
        String message = Utility.getOutputMessage(EntityName.PRODUCT, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private ProductResponse returnObjectResponse(Product from, UserPrincipal principal) {
        ProductResponse productResponse = null;
        if (principal.hasAnyRole(FULL_ACCESS_ROLES_ARR))
            productResponse = productMapper.toFullResponse(from);
        else
            productResponse = productMapper.toResponse(from);

        return productResponse;
    }
}
