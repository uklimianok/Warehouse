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

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.product.productPallet.ProductPalletRequest;
import com.warehouse.demo.dto.product.productPallet.ProductPalletResponse;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.mapper.product.productPallet.ProductPalletResponseMapper;
import com.warehouse.demo.service.product.ProductPalletService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product-pallets")
@RequiredArgsConstructor
public class ProductPalletController {
    private final ProductPalletService productPalletService;
    private final ProductPalletResponseMapper productPalletResponseMapper;

    private static final String CREATE_ACCESS_ROLES = 
        "hasAnyRole('ORDERS_PROCEEDER', 'SYSTEM_ADMINISTRATOR')";
    private static final String READ_ACCESS_ROLES =
        "hasAnyRole('GOODS_UNLOADER', 'GOODS_PICKER', 'OPERATOR', " +
        "'COORDINATOR', 'DATA_CONTROLLER', 'DIRECTOR', 'DEVELOPER', " +
        "'SYSTEM_ADMINISTRATOR')";
    private static final String READ_UPDATE_ACCESS_ROLES = 
        "hasAnyRole('GOODS_UNLOADER', 'GOODS_PICKER', 'OPERATOR', " +
        "'COORDINATOR', 'DATA_CONTROLLER', 'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES =
        "hasAnyRole('SYSTEM_ADMINISTRATOR')";

    private static final String[] TRANSFER_RESPONSE_ROLES_ARR = 
        {
            "GOODS_UNLOADER", "OPERATOR"
        };
    private static final String[] FULL_RESPONSE_ROLES_ARR =
        {
            "COORDINATOR", "DATA_CONTROLLER", "DIRECTOR", "DEVELOPER", 
            "SYSTEM_ADMINISTRATOR"
        };

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends ProductPalletResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<ProductPallet> productPallets = productPalletService.readAll();
        List<? extends ProductPalletResponse> productPalletsResponse = productPallets
            .stream()
            .map(s -> returnObjectResponse(s, userPrincipal))
            .toList();

        ResponseEntity<List<? extends ProductPalletResponse>> response = new ResponseEntity<>(productPalletsResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends ProductPalletResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        ProductPallet productPallet = productPalletService.read(id);
        ProductPalletResponse productPalletResponse = returnObjectResponse(productPallet, userPrincipal);

        ResponseEntity<ProductPalletResponse> response = new ResponseEntity<>(productPalletResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(CREATE_ACCESS_ROLES)
    public ResponseEntity<? extends ProductPalletResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ProductPalletRequest productPalletRequest) {
        ProductPallet productPallet = productPalletService.create(productPalletRequest);
        ProductPalletResponse productPalletResponse = returnObjectResponse(productPallet, userPrincipal);

        ResponseEntity<ProductPalletResponse> response = new ResponseEntity<>(productPalletResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<? extends ProductPalletResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody ProductPalletRequest productPalletRequest) {
        ProductPallet productPallet = productPalletService.update(id, productPalletRequest);
        ProductPalletResponse productPalletResponse = returnObjectResponse(productPallet, userPrincipal);

        ResponseEntity<ProductPalletResponse> response = new ResponseEntity<>(productPalletResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        productPalletService.delete(id);
        String message = Utility.getOutputMessage(EntityName.PRODUCT_PALLET, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private ProductPalletResponse returnObjectResponse(ProductPallet from, UserPrincipal principal) {
        ProductPalletResponse response = null;
        if (principal.hasAnyRole(FULL_RESPONSE_ROLES_ARR))
            response = productPalletResponseMapper.convertToFullResponse(from);
        else if (principal.hasAnyRole(TRANSFER_RESPONSE_ROLES_ARR))
            response = productPalletResponseMapper.convertToTransferResponse(from);
        else
            response = productPalletResponseMapper.convertToResponse(from);

        return response;
    }
}
