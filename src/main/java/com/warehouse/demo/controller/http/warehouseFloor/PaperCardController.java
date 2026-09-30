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
import com.warehouse.demo.dto.item.paperCard.PaperCardRequest;
import com.warehouse.demo.dto.item.paperCard.PaperCardResponse;
import com.warehouse.demo.entity.item.PaperCard;
import com.warehouse.demo.mapper.item.paperCard.PaperCardResponseMapper;
import com.warehouse.demo.service.item.PaperCardService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/paper-cards")
@RequiredArgsConstructor
public class PaperCardController {
    private final PaperCardService paperCardService;
    private final PaperCardResponseMapper paperCardResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends PaperCardResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        List<PaperCard> paperCards = paperCardService.readAll();
        List<? extends PaperCardResponse> paperCardsResponse = paperCards
            .stream()
            .map(pc -> returnObjectResponse(pc, userPrincipal))
            .toList();

        return new ResponseEntity<>(paperCardsResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends PaperCardResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        PaperCard paperCard = paperCardService.read(id);
        PaperCardResponse paperCardResponse = returnObjectResponse(paperCard, userPrincipal);

        return new ResponseEntity<>(paperCardResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends PaperCardResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody PaperCardRequest paperCardRequest) {
        throwIfUnauthorized('C', userPrincipal.getMainRole());

        PaperCard paperCard = paperCardService.create(paperCardRequest);
        PaperCardResponse paperCardResponse = returnObjectResponse(paperCard, userPrincipal);

        return new ResponseEntity<>(paperCardResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends PaperCardResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody PaperCardRequest paperCardRequest) {
        throwIfUnauthorized('U', userPrincipal.getMainRole());

        PaperCard paperCard = paperCardService.update(id, paperCardRequest);
        PaperCardResponse paperCardResponse = returnObjectResponse(paperCard, userPrincipal);

        return new ResponseEntity<>(paperCardResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('D', userPrincipal.getMainRole());

        paperCardService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.PAPER_CARD, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private PaperCardResponse returnObjectResponse(PaperCard from, UserPrincipal principal) {
        PaperCardResponse response = paperCardResponseMapper.convertToResponse(from);
        return response;
    }

    private void throwIfUnauthorized(char mode, String role) {
        if (!controllerSecurity.getAccessRoles(getClass(), mode).contains(role))
            throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));
    }
}
