package com.warehouse.demo.controller.http.warehouseFloor;

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

import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.item.paperCard.PaperCardRequest;
import com.warehouse.demo.dto.item.paperCard.PaperCardResponse;
import com.warehouse.demo.entity.item.PaperCard;
import com.warehouse.demo.mapper.item.paperCard.PaperCardResponseMapper;
import com.warehouse.demo.service.item.PaperCardService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/paper-cards")
@RequiredArgsConstructor
public class PaperCardController {
    private final PaperCardService paperCardService;
    private final PaperCardResponseMapper paperCardResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends PaperCardResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<PaperCard> paperCards = paperCardService.readAll();
        List<? extends PaperCardResponse> paperCardsResponse = paperCards
            .stream()
            .map(pc -> returnObjectResponse(pc, userPrincipal))
            .toList();

        return new ResponseEntity<>(paperCardsResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends PaperCardResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        PaperCard paperCard = paperCardService.read(id);
        PaperCardResponse paperCardResponse = returnObjectResponse(paperCard, userPrincipal);

        return new ResponseEntity<>(paperCardResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends PaperCardResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid PaperCardRequest paperCardRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');

        PaperCard paperCard = paperCardService.create(paperCardRequest);
        PaperCardResponse paperCardResponse = returnObjectResponse(paperCard, userPrincipal);

        return new ResponseEntity<>(paperCardResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends PaperCardResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid PaperCardRequest paperCardRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');

        PaperCard paperCard = paperCardService.update(id, paperCardRequest);
        PaperCardResponse paperCardResponse = returnObjectResponse(paperCard, userPrincipal);

        return new ResponseEntity<>(paperCardResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        paperCardService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.PAPER_CARD, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private PaperCardResponse returnObjectResponse(
        PaperCard from, 
        UserPrincipal principal
    ) {
        PaperCardResponse response = paperCardResponseMapper.convertToResponse(from);
        return response;
    }
}
