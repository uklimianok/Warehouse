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
import local.warehouse.app.dto.order.returnProduct.ReturnProductRequest;
import local.warehouse.app.dto.order.returnProduct.ReturnProductResponse;
import local.warehouse.app.entity.order.ReturnProduct;
import local.warehouse.app.mapper.order.returnProduct.ReturnProductResponseMapper;
import local.warehouse.app.service.order.ReturnProductService;
import local.warehouse.app.util.action.ControllerSecurity;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/return-products")
@RequiredArgsConstructor
public class ReturnProductController {
    private final ReturnProductService returnProductService;
    private final ReturnProductResponseMapper returnProductResponseMapper;
    
    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends ReturnProductResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<ReturnProduct> returnProducts = returnProductService.readAll();
        List<? extends ReturnProductResponse> returnProductResponse = returnProducts
            .stream()
            .map(rp -> returnObjectResponse(rp, userPrincipal))
            .toList();

        return new ResponseEntity<>(returnProductResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends ReturnProductResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        ReturnProduct returnProduct = returnProductService.read(id);
        ReturnProductResponse returnProductResponse = returnObjectResponse(returnProduct, userPrincipal);

        return new ResponseEntity<>(returnProductResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends ReturnProductResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid ReturnProductRequest returnProductRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');
        
        ReturnProduct returnProduct = returnProductService.create(returnProductRequest);
        ReturnProductResponse returnProductResponse = returnObjectResponse(returnProduct, userPrincipal);

        return new ResponseEntity<>(returnProductResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends ReturnProductResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid ReturnProductRequest returnProductRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');
        
        ReturnProduct returnProduct = returnProductService.update(id, returnProductRequest);
        ReturnProductResponse returnProductResponse = returnObjectResponse(returnProduct, userPrincipal);

        return new ResponseEntity<>(returnProductResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        returnProductService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.RETURN_PRODUCT, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    
    private ReturnProductResponse returnObjectResponse(
        ReturnProduct from, 
        UserPrincipal principal
    ) {
        ReturnProductResponse response = returnProductResponseMapper.convertToResponse(from);
        return response;
    }
}