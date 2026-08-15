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

import com.warehouse.demo.dto.employee.organization.FullOrganizationResponse;
import com.warehouse.demo.dto.employee.organization.organizationType.OrganizationTypeResponse;
import com.warehouse.demo.dto.item.pallet.FullPalletResponse;
import com.warehouse.demo.dto.item.pallet.PalletResponse;
import com.warehouse.demo.dto.product.FullProductResponse;
import com.warehouse.demo.dto.product.ProductResponse;
import com.warehouse.demo.dto.product.productPackage.ProductPackageResponse;
import com.warehouse.demo.dto.product.productPallet.FullProductPalletResponse;
import com.warehouse.demo.dto.product.productPallet.ProductPalletRequest;
import com.warehouse.demo.dto.product.productPallet.ProductPalletResponse;
import com.warehouse.demo.dto.product.productPallet.TransferProductPalletResponse;
import com.warehouse.demo.dto.service.status.StatusResponse;
import com.warehouse.demo.dto.workplace.workStation.FullWorkStationResponse;
import com.warehouse.demo.dto.workplace.workshop.WorkshopResponse;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.product.ProductPalletService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product_pallets")
@RequiredArgsConstructor
public class ProductPalletController {
    private final ProductPalletService productPalletService;

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
    public ResponseEntity<ProductPalletResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        ProductPallet productPallet = productPalletService.read(id);
        ProductPalletResponse productPalletResponse = returnObjectResponse(productPallet, userPrincipal);

        ResponseEntity<ProductPalletResponse> response = new ResponseEntity<>(productPalletResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(CREATE_ACCESS_ROLES)
    public ResponseEntity<ProductPalletResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ProductPalletRequest productPalletRequest) {
        ProductPallet productPallet = productPalletService.create(productPalletRequest);
        ProductPalletResponse productPalletResponse = returnObjectResponse(productPallet, userPrincipal);

        ResponseEntity<ProductPalletResponse> response = new ResponseEntity<>(productPalletResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(READ_UPDATE_ACCESS_ROLES)
    public ResponseEntity<ProductPalletResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody ProductPalletRequest productPalletRequest) {
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

    private ProductPalletResponse returnObjectResponse(ProductPallet from, UserPrincipal userPrincipal) {
        ProductPalletResponse productPalletResponse = null;
        if (userPrincipal.hasAnyRole(FULL_RESPONSE_ROLES_ARR))
            productPalletResponse = new FullProductPalletResponse(
                from.getId(),
                new ProductPackageResponse(
                    from.getProductPackage().getId(),
                    new FullProductResponse(
                        from.getProductPackage().getProduct().getId(),
                        from.getProductPackage().getProduct().getName(),
                        from.getProductPackage().getProduct().getBarcodeNumber(),
                        from.getProductPackage().getProduct().getCost(),
                        new FullOrganizationResponse(
                            from.getProductPackage().getProduct().getProducer().getId(),
                            from.getProductPackage().getProduct().getProducer().getName(),
                            from.getProductPackage().getProduct().getProducer().getOrganizationNumber(),
                            new OrganizationTypeResponse(
                                from.getProductPackage().getProduct().getProducer().getOrganizationType().getId(),
                                from.getProductPackage().getProduct().getProducer().getOrganizationType().getName()
                            ),
                            from.getProductPackage().getProduct().getProducer().getAddress(),
                            from.getProductPackage().getProduct().getProducer().getPhoneNumber(),
                            from.getProductPackage().getProduct().getProducer().getEmail(),
                            from.getProductPackage().getProduct().getProducer().getUrl()
                        )
                    ),
                    from.getProductPackage().getProductsAmount(),
                    from.getProductPackage().getVolume(),
                    from.getProductPackage().getWeight()
                ),
                from.getPackageAmount(),
                from.getPallet() != null ? new FullPalletResponse(
                    from.getPallet().getId(),
                    from.getPallet().getName(),
                    from.getPallet().getColor(),
                    from.getPallet().getLength(),
                    from.getPallet().getWidth(),
                    from.getPallet().getHeight(),
                    from.getPallet().getWeight()
                ) : null,
                from.getPalletNumber(),
                from.getGroupNumber(),
                new StatusResponse(
                    from.getStatus().getId(),
                    from.getStatus().getName(),
                    from.getStatus().getType()
                ),
                from.getWorkStation() != null ? new FullWorkStationResponse(
                    from.getWorkStation().getId(),
                    from.getWorkStation().getStationNumber(),
                    from.getWorkStation().getControlNumber(),
                    from.getWorkStation().getType(),
                    new WorkshopResponse(
                        from.getWorkStation().getWorkshop().getId(),
                        from.getWorkStation().getWorkshop().getName(),
                        from.getWorkStation().getWorkshop().getStandard()
                    )
                ) : null,
                from.getNextWorkStation() != null ? new FullWorkStationResponse(
                    from.getNextWorkStation().getId(),
                    from.getNextWorkStation().getStationNumber(),
                    from.getNextWorkStation().getControlNumber(),
                    from.getNextWorkStation().getType(),
                    new WorkshopResponse(
                        from.getNextWorkStation().getWorkshop().getId(),
                        from.getNextWorkStation().getWorkshop().getName(),
                        from.getNextWorkStation().getWorkshop().getStandard()
                    )
                ) : null
            );
        else if (userPrincipal.hasAnyRole(TRANSFER_RESPONSE_ROLES_ARR))
            productPalletResponse = new TransferProductPalletResponse(
                from.getId(),
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
                from.getPallet() != null ? new PalletResponse(
                    from.getPallet().getId(),
                    from.getPallet().getName(),
                    from.getPallet().getColor()
                ) : null,
                from.getPalletNumber(),
                from.getGroupNumber(),
                from.getWorkStation() != null ? new FullWorkStationResponse(
                    from.getWorkStation().getId(),
                    from.getWorkStation().getStationNumber(),
                    from.getWorkStation().getControlNumber(),
                    from.getWorkStation().getType(),
                    new WorkshopResponse(
                        from.getWorkStation().getWorkshop().getId(),
                        from.getWorkStation().getWorkshop().getName(),
                        from.getWorkStation().getWorkshop().getStandard()
                    )
                ) : null,
                from.getNextWorkStation() != null ? new FullWorkStationResponse(
                    from.getNextWorkStation().getId(),
                    from.getNextWorkStation().getStationNumber(),
                    from.getNextWorkStation().getControlNumber(),
                    from.getNextWorkStation().getType(),
                    new WorkshopResponse(
                        from.getNextWorkStation().getWorkshop().getId(),
                        from.getNextWorkStation().getWorkshop().getName(),
                        from.getNextWorkStation().getWorkshop().getStandard()
                    )
                ) : null
            );
        else 
            productPalletResponse = new ProductPalletResponse(
                from.getId(),
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
                from.getPalletNumber(),
                from.getGroupNumber(),
                from.getWorkStation() != null ? new FullWorkStationResponse(
                    from.getWorkStation().getId(),
                    from.getWorkStation().getStationNumber(),
                    from.getWorkStation().getControlNumber(),
                    from.getWorkStation().getType(),
                    new WorkshopResponse(
                        from.getWorkStation().getWorkshop().getId(),
                        from.getWorkStation().getWorkshop().getName(),
                        from.getWorkStation().getWorkshop().getStandard()
                    )
                ) : null
            );

        return productPalletResponse;
    }
}
