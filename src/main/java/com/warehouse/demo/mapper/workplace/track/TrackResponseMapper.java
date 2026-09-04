package com.warehouse.demo.mapper.workplace.track;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.warehouse.demo.dto.workplace.track.FullTrackResponse;
import com.warehouse.demo.dto.workplace.track.TrackResponse;
import com.warehouse.demo.entity.workplace.Track;
import com.warehouse.demo.mapper.workplace.gate.GateResponseMapper;

@Mapper(componentModel = "spring", uses = GateResponseMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TrackResponseMapper {
    TrackResponse convertToResponse(Track track);
    FullTrackResponse convertToFullResponse(Track track);
}
