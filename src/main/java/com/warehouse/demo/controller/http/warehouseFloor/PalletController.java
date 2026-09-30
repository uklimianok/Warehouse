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
import com.warehouse.demo.dto.item.pallet.PalletRequest;
import com.warehouse.demo.dto.item.pallet.PalletResponse;
import com.warehouse.demo.entity.item.Pallet;
import com.warehouse.demo.mapper.item.pallet.PalletResponseMapper;
import com.warehouse.demo.service.item.PalletService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pallets")
@RequiredArgsConstructor
public class PalletController {
    private final PalletService palletService;
    private final PalletResponseMapper palletResponseMapper;

    private final ControllerSecurity controllerSecurity;

    private static final String[] FULL_ACCESS_ROLES_ARR = 
        {"DATA_CONTROLLER", "SYSTEM_ADMINISTRATOR"};

    @GetMapping
    public ResponseEntity<List<? extends PalletResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        List<Pallet> pallets = palletService.readAll();
        List<PalletResponse> palletResponse = pallets
            .stream()
            .map(p -> returnObjectResponse(p, userPrincipal))
            .toList();

        return new ResponseEntity<>(palletResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends PalletResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());
        
        Pallet pallet = palletService.read(id);
        PalletResponse palletResponse = returnObjectResponse(pallet, userPrincipal);

        return new ResponseEntity<>(palletResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends PalletResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody PalletRequest palletRequest) {
        throwIfUnauthorized('C', userPrincipal.getMainRole());

        Pallet pallet = palletService.create(palletRequest);
        PalletResponse palletResponse = returnObjectResponse(pallet, userPrincipal);

        return new ResponseEntity<>(palletResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends PalletResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody PalletRequest palletRequest) {
        throwIfUnauthorized('U', userPrincipal.getMainRole());

        Pallet pallet = palletService.update(id, palletRequest);
        PalletResponse palletResponse = returnObjectResponse(pallet, userPrincipal);

        return new ResponseEntity<>(palletResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('D', userPrincipal.getMainRole());

        palletService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.PALLET, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private PalletResponse returnObjectResponse(Pallet from, UserPrincipal principal) {
        PalletResponse response = null;
        if (principal.hasAnyRole(FULL_ACCESS_ROLES_ARR))
            response = palletResponseMapper.convertToFullResponse(from);
        else
            response = palletResponseMapper.convertToResponse(from);

        return response;
    }

    private void throwIfUnauthorized(char mode, String role) {
        if (!controllerSecurity.getAccessRoles(getClass(), mode).contains(role))
            throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));
    }
}
