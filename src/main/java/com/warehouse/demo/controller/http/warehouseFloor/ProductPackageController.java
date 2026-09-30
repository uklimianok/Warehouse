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
import com.warehouse.demo.dto.product.productPackage.ProductPackageRequest;
import com.warehouse.demo.dto.product.productPackage.ProductPackageResponse;
import com.warehouse.demo.entity.product.ProductPackage;
import com.warehouse.demo.mapper.product.productPackage.ProductPackageResponseMapper;
import com.warehouse.demo.service.product.ProductPackageService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/packages")
@RequiredArgsConstructor
public class ProductPackageController {
    private final ProductPackageService productPackageService;
    private final ProductPackageResponseMapper productPackageResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends ProductPackageResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        List<ProductPackage> productPackages = productPackageService.readAll();
        List<? extends ProductPackageResponse> productPackageResponse = productPackages
            .stream()
            .map(pp -> returnObjectResponse(pp, userPrincipal))
            .toList();

        return new ResponseEntity<>(productPackageResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends ProductPackageResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        ProductPackage productPackage = productPackageService.read(id);
        ProductPackageResponse productPackageResponse = returnObjectResponse(productPackage, userPrincipal);

        return new ResponseEntity<>(productPackageResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends ProductPackageResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ProductPackageRequest productPackageRequest) {
        throwIfUnauthorized('C', userPrincipal.getMainRole());

        ProductPackage productPackage = productPackageService.create(productPackageRequest);
        ProductPackageResponse productPackageResponse = returnObjectResponse(productPackage, userPrincipal);

        return new ResponseEntity<>(productPackageResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends ProductPackageResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody ProductPackageRequest productPackageRequest) {
        throwIfUnauthorized('U', userPrincipal.getMainRole());

        ProductPackage productPackage = productPackageService.update(id, productPackageRequest);
        ProductPackageResponse productPackageResponse = returnObjectResponse(productPackage, userPrincipal);

        return new ResponseEntity<>(productPackageResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('D', userPrincipal.getMainRole());

        productPackageService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.PRODUCT_PACKAGE, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private ProductPackageResponse returnObjectResponse(ProductPackage from, UserPrincipal principal) {
        ProductPackageResponse response = productPackageResponseMapper.convertToResponse(from);
        return response;
    }

    private void throwIfUnauthorized(char mode, String role) {
        if (!controllerSecurity.getAccessRoles(getClass(), mode).contains(role))
            throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));
    }
}
