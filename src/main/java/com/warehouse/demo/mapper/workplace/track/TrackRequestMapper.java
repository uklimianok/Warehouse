package com.warehouse.demo.mapper.workplace.track;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.demo.dto.workplace.track.TrackRequest;
import com.warehouse.demo.entity.workplace.Track;
import com.warehouse.demo.mapper.workplace.gate.GateResolver;

@Mapper(componentModel = "spring", uses = GateResolver.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TrackRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gate", source = "trackRequest.gateId")
    void convertFromRequest(TrackRequest trackRequest, @MappingTarget Track track);
}
