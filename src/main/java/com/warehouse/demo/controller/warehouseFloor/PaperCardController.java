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
import com.warehouse.demo.dto.item.pallet.PalletResponse;
import com.warehouse.demo.dto.item.paperCard.PaperCardRequest;
import com.warehouse.demo.dto.item.paperCard.PaperCardResponse;
import com.warehouse.demo.dto.order.OrderResponse;
import com.warehouse.demo.dto.order.orderPallet.OrderPalletResponse;
import com.warehouse.demo.dto.workplace.gate.GateResponse;
import com.warehouse.demo.entity.item.PaperCard;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.item.PaperCardService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/paper-cards")
@RequiredArgsConstructor
public class PaperCardController {
    private final PaperCardService paperCardService;

    private static final String READ_ACCESS_ROLES =
        "hasAnyRole('GOODS_PICKER', 'SET_GOODS_EXPORTER', " +
        "'SET_GOODS_LOADER', 'COORDINATOR', 'DATA_CONTROLLER', " +
        "'DIRECTOR', 'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String CREATE_READ_ACCESS_ROLES =
        "hasAnyRole('GOODS_PICKER', 'COORDINATOR', 'DATA_CONTROLLER', " +
        "'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES =
        "hasAnyRole('DATA_CONTROLLER', 'SYSTEM_ADMINISTRATOR')";

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends PaperCardResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<PaperCard> paperCards = paperCardService.readAll();
        List<? extends PaperCardResponse> paperCardsResponse = paperCards
            .stream()
            .map(pc -> returnObjectResponse(pc, userPrincipal))
            .toList();

        ResponseEntity<List<? extends PaperCardResponse>> response = new ResponseEntity<>(paperCardsResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends PaperCardResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        PaperCard paperCard = paperCardService.read(id);
        PaperCardResponse paperCardResponse = returnObjectResponse(paperCard, userPrincipal);

        ResponseEntity<? extends PaperCardResponse> response = new ResponseEntity<>(paperCardResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(CREATE_READ_ACCESS_ROLES)
    public ResponseEntity<? extends PaperCardResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody PaperCardRequest paperCardRequest) {
        PaperCard paperCard = paperCardService.create(paperCardRequest);
        PaperCardResponse paperCardResponse = returnObjectResponse(paperCard, userPrincipal);

        ResponseEntity<? extends PaperCardResponse> response = new ResponseEntity<>(paperCardResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends PaperCardResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody PaperCardRequest paperCardRequest) {
        PaperCard paperCard = paperCardService.update(id, paperCardRequest);
        PaperCardResponse paperCardResponse = returnObjectResponse(paperCard, userPrincipal);

        ResponseEntity<? extends PaperCardResponse> response = new ResponseEntity<>(paperCardResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        paperCardService.delete(id);
        String message = Utility.getOutputMessage(EntityName.PAPER_CARD, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private PaperCardResponse returnObjectResponse(PaperCard from, UserPrincipal userPrincipal) {
        PaperCardResponse paperCardResponse = new PaperCardResponse(
            from.getId(),
            from.getCode(),
            new OrderPalletResponse(
                from.getOrderPallet().getId(),
                new OrderResponse(
                    from.getOrderPallet().getOrder().getId(),
                    new OrganizationResponse(
                        from.getOrderPallet().getOrder().getStore().getId(),
                        from.getOrderPallet().getOrder().getStore().getName(),
                        from.getOrderPallet().getOrder().getStore().getOrganizationNumber()
                    ),
                    from.getOrderPallet().getOrder().getGate() != null ? new GateResponse(
                        from.getOrderPallet().getOrder().getGate().getId(),
                        from.getOrderPallet().getOrder().getGate().getSymbol()
                    ) : null,
                    from.getOrderPallet().getOrder().getNote()
                ),
                new PalletResponse(
                    from.getOrderPallet().getPallet().getId(),
                    from.getOrderPallet().getPallet().getName(),
                    from.getOrderPallet().getPallet().getColor()
                )
            )
        );

        return paperCardResponse;
    }
}
