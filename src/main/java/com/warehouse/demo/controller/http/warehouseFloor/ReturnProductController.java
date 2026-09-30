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
import com.warehouse.demo.dto.order.returnProduct.ReturnProductRequest;
import com.warehouse.demo.dto.order.returnProduct.ReturnProductResponse;
import com.warehouse.demo.entity.order.ReturnProduct;
import com.warehouse.demo.mapper.order.returnProduct.ReturnProductResponseMapper;
import com.warehouse.demo.service.order.ReturnProductService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/return-products")
@RequiredArgsConstructor
public class ReturnProductController {
    private final ReturnProductService returnProductService;
    private final ReturnProductResponseMapper returnProductResponseMapper;
    
    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends ReturnProductResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        List<ReturnProduct> returnProducts = returnProductService.readAll();
        List<? extends ReturnProductResponse> returnProductResponse = returnProducts
            .stream()
            .map(rp -> returnObjectResponse(rp, userPrincipal))
            .toList();

        return new ResponseEntity<>(returnProductResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends ReturnProductResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        ReturnProduct returnProduct = returnProductService.read(id);
        ReturnProductResponse returnProductResponse = returnObjectResponse(returnProduct, userPrincipal);

        return new ResponseEntity<>(returnProductResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends ReturnProductResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ReturnProductRequest returnProductRequest) {
        throwIfUnauthorized('C', userPrincipal.getMainRole());
        
        ReturnProduct returnProduct = returnProductService.create(returnProductRequest);
        ReturnProductResponse returnProductResponse = returnObjectResponse(returnProduct, userPrincipal);

        return new ResponseEntity<>(returnProductResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends ReturnProductResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody ReturnProductRequest returnProductRequest) {
        throwIfUnauthorized('U', userPrincipal.getMainRole());
        
        ReturnProduct returnProduct = returnProductService.update(id, returnProductRequest);
        ReturnProductResponse returnProductResponse = returnObjectResponse(returnProduct, userPrincipal);

        return new ResponseEntity<>(returnProductResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('D', userPrincipal.getMainRole());

        returnProductService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.RETURN_PRODUCT, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    
    private ReturnProductResponse returnObjectResponse(ReturnProduct from, UserPrincipal principal) {
        ReturnProductResponse response = returnProductResponseMapper.convertToResponse(from);
        return response;
    }

    private void throwIfUnauthorized(char mode, String role) {
        if (!controllerSecurity.getAccessRoles(getClass(), mode).contains(role))
            throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));
    }
}