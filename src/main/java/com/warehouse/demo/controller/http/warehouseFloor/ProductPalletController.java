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
import com.warehouse.demo.dto.product.productPallet.ProductPalletRequest;
import com.warehouse.demo.dto.product.productPallet.ProductPalletResponse;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.mapper.product.productPallet.ProductPalletResponseMapper;
import com.warehouse.demo.service.product.ProductPalletService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product-pallets")
@RequiredArgsConstructor
public class ProductPalletController {
    private final ProductPalletService productPalletService;
    private final ProductPalletResponseMapper productPalletResponseMapper;

    private final ControllerSecurity controllerSecurity;

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
    public ResponseEntity<List<? extends ProductPalletResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());
        
        List<ProductPallet> productPallets = productPalletService.readAll();
        List<? extends ProductPalletResponse> productPalletsResponse = productPallets
            .stream()
            .map(s -> returnObjectResponse(s, userPrincipal))
            .toList();

        return new ResponseEntity<>(productPalletsResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends ProductPalletResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        ProductPallet productPallet = productPalletService.read(id);
        ProductPalletResponse productPalletResponse = returnObjectResponse(productPallet, userPrincipal);

        return new ResponseEntity<>(productPalletResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends ProductPalletResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ProductPalletRequest productPalletRequest) {
        throwIfUnauthorized('C', userPrincipal.getMainRole());

        ProductPallet productPallet = productPalletService.create(productPalletRequest);
        ProductPalletResponse productPalletResponse = returnObjectResponse(productPallet, userPrincipal);

        return new ResponseEntity<>(productPalletResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends ProductPalletResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody ProductPalletRequest productPalletRequest) {
        throwIfUnauthorized('U', userPrincipal.getMainRole());

        ProductPallet productPallet = productPalletService.update(id, productPalletRequest, userPrincipal);
        ProductPalletResponse productPalletResponse = returnObjectResponse(productPallet, userPrincipal);

        return new ResponseEntity<>(productPalletResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('D', userPrincipal.getMainRole());

        productPalletService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.PRODUCT_PALLET, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
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

    private void throwIfUnauthorized(char mode, String role) {
        if (!controllerSecurity.getAccessRoles(getClass(), mode).contains(role))
            throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));
    }
}
