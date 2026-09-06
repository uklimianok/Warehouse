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
import com.warehouse.demo.dto.item.pallet.PalletRequest;
import com.warehouse.demo.dto.item.pallet.PalletResponse;
import com.warehouse.demo.entity.item.Pallet;
import com.warehouse.demo.mapper.item.pallet.PalletResponseMapper;
import com.warehouse.demo.service.item.PalletService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pallets")
@RequiredArgsConstructor
public class PalletController {
    private final PalletService palletService;
    private final PalletResponseMapper palletResponseMapper;

    private static final String READ_ACCESS_ROLES = 
        "hasAnyRole('GOODS_UNLOADER', 'GOODS_PICKER', 'SET_GOODS_EXPORTER', " +
        "'SET_GOODS_LOADER', 'COORDINATOR', 'DATA_CONTROLLER', " +
        "'DIRECTOR', 'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES = 
        "hasAnyRole('DATA_CONTROLLER', 'SYSTEM_ADMINISTRATOR')";

    private static final String[] FULL_ACCESS_ROLES_ARR = 
        {"DATA_CONTROLLER", "SYSTEM_ADMINISTRATOR"};

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends PalletResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Pallet> pallets = palletService.readAll();
        List<PalletResponse> palletResponse = pallets
            .stream()
            .map(p -> returnObjectResponse(p, userPrincipal))
            .toList();

        ResponseEntity<List<? extends PalletResponse>> response = new ResponseEntity<>(palletResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends PalletResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        Pallet pallet = palletService.read(id);
        PalletResponse palletResponse = returnObjectResponse(pallet, userPrincipal);

        ResponseEntity<PalletResponse> response = new ResponseEntity<>(palletResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends PalletResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody PalletRequest palletRequest) {
        Pallet pallet = palletService.create(palletRequest);
        PalletResponse palletResponse = returnObjectResponse(pallet, userPrincipal);

        ResponseEntity<PalletResponse> response = new ResponseEntity<>(palletResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends PalletResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody PalletRequest palletRequest) {
        Pallet pallet = palletService.update(id, palletRequest);
        PalletResponse palletResponse = returnObjectResponse(pallet, userPrincipal);

        ResponseEntity<PalletResponse> response = new ResponseEntity<>(palletResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        palletService.delete(id);
        String message = Utility.getOutputMessage(EntityName.PALLET, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private PalletResponse returnObjectResponse(Pallet from, UserPrincipal principal) {
        PalletResponse response = null;
        if (principal.hasAnyRole(FULL_ACCESS_ROLES_ARR))
            response = palletResponseMapper.convertToFullResponse(from);
        else
            response = palletResponseMapper.convertToResponse(from);

        return response;
    }
}
