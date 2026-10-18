package local.warehouse.app.controller.http.warehouseFloor;

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

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import local.warehouse.app.configuration.security.UserPrincipal;
import local.warehouse.app.dto.product.productPallet.ProductPalletRequest;
import local.warehouse.app.dto.product.productPallet.ProductPalletResponse;
import local.warehouse.app.entity.product.ProductPallet;
import local.warehouse.app.mapper.product.productPallet.ProductPalletResponseMapper;
import local.warehouse.app.service.product.ProductPalletService;
import local.warehouse.app.util.action.ControllerSecurity;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product-pallets")
@RequiredArgsConstructor
public class ProductPalletController {
    private final ProductPalletService productPalletService;
    private final ProductPalletResponseMapper productPalletResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends ProductPalletResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');
        
        List<ProductPallet> productPallets = productPalletService.readAll();
        List<? extends ProductPalletResponse> productPalletsResponse = productPallets
            .stream()
            .map(s -> returnObjectResponse(s, userPrincipal))
            .toList();

        return new ResponseEntity<>(productPalletsResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends ProductPalletResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        ProductPallet productPallet = productPalletService.read(id);
        ProductPalletResponse productPalletResponse = returnObjectResponse(productPallet, userPrincipal);

        return new ResponseEntity<>(productPalletResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends ProductPalletResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid ProductPalletRequest productPalletRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');

        ProductPallet productPallet = productPalletService.create(productPalletRequest);
        ProductPalletResponse productPalletResponse = returnObjectResponse(productPallet, userPrincipal);

        return new ResponseEntity<>(productPalletResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends ProductPalletResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid ProductPalletRequest productPalletRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');

        ProductPallet productPallet = productPalletService.update(id, productPalletRequest, userPrincipal);
        ProductPalletResponse productPalletResponse = returnObjectResponse(productPallet, userPrincipal);

        return new ResponseEntity<>(productPalletResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        productPalletService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.PRODUCT_PALLET, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private ProductPalletResponse returnObjectResponse(
        ProductPallet from, 
        UserPrincipal principal
    ) {
        String responseType = controllerSecurity.getResponseObjectType(getClass(), principal.getEmployeeNumber());
        return switch (responseType) {
            case "Full" -> productPalletResponseMapper.convertToFullResponse(from);
            case "Transfer" -> productPalletResponseMapper.convertToTransferResponse(from);
            default -> productPalletResponseMapper.convertToResponse(from);
        };
    }
}
