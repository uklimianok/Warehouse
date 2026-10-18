package local.warehouse.app.mapper.workplace.track;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.workplace.track.FullTrackResponse;
import local.warehouse.app.dto.workplace.track.TrackResponse;
import local.warehouse.app.entity.workplace.Track;
import local.warehouse.app.mapper.workplace.gate.GateResponseMapper;

@Mapper(
    componentModel = "spring", 
    uses = GateResponseMapper.class, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface TrackResponseMapper {
    TrackResponse convertToResponse(Track track);
    FullTrackResponse convertToFullResponse(Track track);
}
