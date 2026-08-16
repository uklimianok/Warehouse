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
import com.warehouse.demo.dto.product.FullProductResponse;
import com.warehouse.demo.dto.product.ProductResponse;
import com.warehouse.demo.dto.product.productPackage.ProductPackageRequest;
import com.warehouse.demo.dto.product.productPackage.ProductPackageResponse;
import com.warehouse.demo.entity.product.ProductPackage;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.product.ProductPackageService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/packages")
@RequiredArgsConstructor
public class ProductPackageController {
    private final ProductPackageService productPackageService;

    private static final String READ_ACCESS_ROLES = 
        "hasAnyRole('GOODS_PICKER', 'COORDINATOR', 'DATA_CONTROLLER', " +
        "'DIRECTOR', 'ORDERS_PROCEEDER', 'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String READ_UPDATE_ACCESS_ROLES = 
        "hasAnyRole('COORDINATOR', 'DATA_CONTROLLER', 'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES = 
        "hasAnyRole('DATA_CONTROLLER', 'SYSTEM_ADMINISTRATOR')";

    private static final String[] READ_UPDATE_ACCESS_ROLES_ARR = 
        {"COORDINATOR", "DATA_CONTROLLER", "SYSTEM_ADMINISTRATOR"};

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends ProductPackageResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<ProductPackage> productPackages = productPackageService.readAll();
        List<? extends ProductPackageResponse> productPackageResponse = productPackages
            .stream()
            .map(pp -> returnObjectResponse(pp, userPrincipal))
            .toList();

        ResponseEntity<List<? extends ProductPackageResponse>> response = new ResponseEntity<>(productPackageResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends ProductPackageResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        ProductPackage productPackage = productPackageService.read(id);
        ProductPackageResponse productPackageResponse = returnObjectResponse(productPackage, userPrincipal);

        ResponseEntity<ProductPackageResponse> response = new ResponseEntity<>(productPackageResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends ProductPackageResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ProductPackageRequest productPackageRequest) {
        ProductPackage productPackage = productPackageService.create(productPackageRequest);
        ProductPackageResponse productPackageResponse = returnObjectResponse(productPackage, userPrincipal);

        ResponseEntity<ProductPackageResponse> response = new ResponseEntity<>(productPackageResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<? extends ProductPackageResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody ProductPackageRequest productPackageRequest) {
        ProductPackage productPackage = productPackageService.update(id, productPackageRequest);
        ProductPackageResponse productPackageResponse = returnObjectResponse(productPackage, userPrincipal);

        ResponseEntity<ProductPackageResponse> response = new ResponseEntity<>(productPackageResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        productPackageService.delete(id);
        String message = Utility.getOutputMessage(EntityName.PRODUCT_PACKAGE, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private ProductPackageResponse returnObjectResponse(ProductPackage from, UserPrincipal userPrincipal) {
        ProductPackageResponse productPackageResponse = new ProductPackageResponse(
            from.getId(),
            userPrincipal.hasAnyRole(READ_UPDATE_ACCESS_ROLES_ARR) ? 
                new FullProductResponse(
                    from.getProduct().getId(),
                    from.getProduct().getName(),
                    from.getProduct().getBarcodeNumber(),
                    from.getProduct().getCost(),
                    new OrganizationResponse(
                        from.getProduct().getProducer().getId(),
                        from.getProduct().getProducer().getName(),
                        from.getProduct().getProducer().getOrganizationNumber()
                    )
                ) : 
                new ProductResponse(
                    from.getProduct().getId(),
                    from.getProduct().getName()
                ),
                from.getProductsAmount(),
                from.getVolume(),
                from.getWeight()
            );

        return productPackageResponse;
    }
}
