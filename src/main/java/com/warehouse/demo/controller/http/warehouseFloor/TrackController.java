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
import com.warehouse.demo.dto.workplace.track.TrackRequest;
import com.warehouse.demo.dto.workplace.track.TrackResponse;
import com.warehouse.demo.entity.workplace.Track;
import com.warehouse.demo.mapper.workplace.track.TrackResponseMapper;
import com.warehouse.demo.service.workplace.TrackService;
import com.warehouse.demo.util.action.ControllerSecurity;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tracks")
@RequiredArgsConstructor
public class TrackController {
    private final TrackService trackService;
    private final TrackResponseMapper trackResponseMapper;

    private final ControllerSecurity controllerSecurity;

    @GetMapping
    public ResponseEntity<List<? extends TrackResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());

        List<Track> tracks = trackService.readAll();
        List<TrackResponse> tracksResponse = tracks
            .stream()
            .map(p -> returnObjectResponse(p, userPrincipal))
            .toList();

        return new ResponseEntity<>(tracksResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends TrackResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('R', userPrincipal.getMainRole());
        
        Track track = trackService.read(id);
        TrackResponse trackResponse = returnObjectResponse(track, userPrincipal);

        return new ResponseEntity<>(trackResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<? extends TrackResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody TrackRequest trackRequest) {
        throwIfUnauthorized('C', userPrincipal.getMainRole());
        
        Track track = trackService.create(trackRequest);
        TrackResponse trackResponse = returnObjectResponse(track, userPrincipal);

        return new ResponseEntity<>(trackResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<? extends TrackResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody TrackRequest trackRequest) {
        throwIfUnauthorized('U', userPrincipal.getMainRole());
        
        Track track = trackService.update(id, trackRequest);
        TrackResponse trackResponse = returnObjectResponse(track, userPrincipal);

        return new ResponseEntity<>(trackResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        throwIfUnauthorized('D', userPrincipal.getMainRole());
        
        trackService.delete(id);
        String message = MessageHandler.getOutputMessage(Entity.TRACK, OutputMessage.DELETED);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private TrackResponse returnObjectResponse(Track from, UserPrincipal principal) {
        String responseType = controllerSecurity.getResponseObjectType(getClass(), principal.getEmployeeNumber());
        return switch (responseType) {
            case "Full" -> trackResponseMapper.convertToFullResponse(from);
            default -> trackResponseMapper.convertToResponse(from);
        };
    }

    private void throwIfUnauthorized(char mode, String role) {
        if (!controllerSecurity.getAccessRoles(getClass(), mode).contains(role))
            throw new AccessDeniedException(MessageHandler.getOutputMessage(OutputMessage.ACCESS_DENIED));
    }
}
