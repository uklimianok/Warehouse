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
import com.warehouse.demo.dto.product.ProductRequest;
import com.warehouse.demo.dto.product.ProductResponse;
import com.warehouse.demo.entity.product.Product;
import com.warehouse.demo.mapper.product.ProductResponseMapper;
import com.warehouse.demo.service.product.ProductService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductResponseMapper productResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends ProductResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<Product> products = productService.readAll();
        List<ProductResponse> productResponses = products
            .stream()
            .map(p -> returnObjectResponse(p, userPrincipal))
            .toList();

        return new ResponseEntity<>(productResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends ProductResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');
        
        Product product = productService.read(id);
        ProductResponse productResponse = returnObjectResponse(product, userPrincipal);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends ProductResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid ProductRequest productRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');

        Product product = productService.create(productRequest);
        ProductResponse productResponse = returnObjectResponse(product, userPrincipal);

        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends ProductResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, @RequestBody @Valid ProductRequest productRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');
        
        Product product = productService.update(id, productRequest);
        ProductResponse productResponse = returnObjectResponse(product, userPrincipal);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        productService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.PRODUCT, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private ProductResponse returnObjectResponse(
        Product from, 
        UserPrincipal principal
    ) {
        String responseType = controllerSecurity.getResponseObjectType(getClass(), principal.getEmployeeNumber());
        return switch (responseType) {
            case "Full" -> productResponseMapper.convertToFullResponse(from);
            default -> productResponseMapper.convertToResponse(from);
        };
    }
}
