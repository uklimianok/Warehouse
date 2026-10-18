package local.warehouse.app.controller.http.officeManagement;

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
import local.warehouse.app.dto.employee.shift.ShiftRequest;
import local.warehouse.app.dto.employee.shift.ShiftResponse;
import local.warehouse.app.entity.employee.Shift;
import local.warehouse.app.mapper.employee.shift.ShiftResponseMapper;
import local.warehouse.app.service.employee.ShiftService;
import local.warehouse.app.util.action.ControllerSecurity;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/shifts")
@RequiredArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;
    private final ShiftResponseMapper shiftResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends ShiftResponse>> readAll(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        List<Shift> shifts = shiftService.readAll();
        List<ShiftResponse> shiftResponses = shifts
            .stream()
            .map(s -> returnObjectResponse(s, userPrincipal))
            .toList();

        return new ResponseEntity<>(shiftResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends ShiftResponse> read(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'R');

        Shift shift = shiftService.read(id);
        ShiftResponse shiftResponse = returnObjectResponse(shift, userPrincipal);

        return new ResponseEntity<>(shiftResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends ShiftResponse> create(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @RequestBody @Valid ShiftRequest shiftRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'C');

        Shift shift = shiftService.create(shiftRequest);
        ShiftResponse shiftResponse = returnObjectResponse(shift, userPrincipal);

        return new ResponseEntity<>(shiftResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends ShiftResponse> update(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id, 
        @RequestBody @Valid ShiftRequest shiftRequest
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'U');

        Shift shift = shiftService.update(id, shiftRequest);
        ShiftResponse shiftResponse = returnObjectResponse(shift, userPrincipal);

        return new ResponseEntity<>(shiftResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
        @AuthenticationPrincipal UserPrincipal userPrincipal, 
        @PathVariable @Positive long id
    ) {
        controllerSecurity.throwIfUnauthorized(getClass(), userPrincipal.getEmployeeNumber(), 'D');

        shiftService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.SHIFT, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private ShiftResponse returnObjectResponse(
        Shift from, 
        UserPrincipal principal
    ) {
        ShiftResponse response = shiftResponseMapper.convertToResponse(from);
        return response;
    }
}
