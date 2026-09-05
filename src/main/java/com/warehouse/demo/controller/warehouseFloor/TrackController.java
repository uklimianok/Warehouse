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

import com.warehouse.demo.dto.workplace.track.TrackRequest;
import com.warehouse.demo.dto.workplace.track.TrackResponse;
import com.warehouse.demo.entity.workplace.Track;
import com.warehouse.demo.mapper.workplace.track.TrackResponseMapper;
import com.warehouse.demo.security.UserPrincipal;
import com.warehouse.demo.service.workplace.TrackService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tracks")
@RequiredArgsConstructor
public class TrackController {
    private final TrackService trackService;
    private final TrackResponseMapper trackResponseMapper;

    private static final String READ_ACCESS_ROLES =
        "hasAnyRole('GOODS_PICKER', 'SET_GOODS_EXPORTER', 'SET_GOODS_LOADER', " +
        "'COORDINATOR', 'DATA_CONTROLLER', 'SHIFT_SUPERVISOR', " +
        "'DIRECTOR', 'DEVELOPER', 'SYSTEM_ADMINISTRATOR')";
    private static final String FULL_ACCESS_ROLES = 
        "hasAnyRole('DATA_CONTROLLER', 'SYSTEM_ADMINISTRATOR')";

    private static final String[] FULL_RESPONSE_ROLES_ARR =
        {
            "DATA_CONTROLLER", "SYSTEM_ADMINISTRATOR"
        };

    @GetMapping
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<List<? extends TrackResponse>> readAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Track> tracks = trackService.readAll();
        List<TrackResponse> tracksResponse = tracks
            .stream()
            .map(p -> returnObjectResponse(p, userPrincipal))
            .toList();

        ResponseEntity<List<? extends TrackResponse>> response = new ResponseEntity<>(tracksResponse, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize(READ_ACCESS_ROLES)
    public ResponseEntity<? extends TrackResponse> read(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id) {
        Track track = trackService.read(id);
        TrackResponse trackResponse = returnObjectResponse(track, userPrincipal);

        ResponseEntity<TrackResponse> response = new ResponseEntity<>(trackResponse, HttpStatus.OK);
        return response;
    }

    @PostMapping
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends TrackResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody TrackRequest trackRequest) {
        Track track = trackService.create(trackRequest);
        TrackResponse trackResponse = returnObjectResponse(track, userPrincipal);

        ResponseEntity<TrackResponse> response = new ResponseEntity<>(trackResponse, HttpStatus.CREATED);
        return response;
    }

    @PatchMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<? extends TrackResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable long id, @RequestBody TrackRequest trackRequest) {
        Track track = trackService.update(id, trackRequest);
        TrackResponse trackResponse = returnObjectResponse(track, userPrincipal);

        ResponseEntity<TrackResponse> response = new ResponseEntity<>(trackResponse, HttpStatus.OK);
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(FULL_ACCESS_ROLES)
    public ResponseEntity<String> delete(@PathVariable long id) {
        trackService.delete(id);
        String message = Utility.getOutputMessage(EntityName.TRACK, OutputMessage.DELETED);

        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    private TrackResponse returnObjectResponse(Track from, UserPrincipal principal) {
        TrackResponse response = null;
        if (principal.hasAnyRole(FULL_RESPONSE_ROLES_ARR))
            response = trackResponseMapper.convertToFullResponse(from);
        else
            response = trackResponseMapper.convertToResponse(from);

        return response;
    }
}
