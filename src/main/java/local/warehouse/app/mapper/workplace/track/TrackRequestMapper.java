package local.warehouse.app.mapper.workplace.track;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.workplace.track.TrackRequest;
import local.warehouse.app.entity.workplace.Track;
import local.warehouse.app.mapper.workplace.gate.GateResolver;

@Mapper(
    componentModel = "spring", 
    uses = GateResolver.class, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR, 
    builder = @Builder(disableBuilder = true)
)
public interface TrackRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gate", source = "trackRequest.gateId")
    void convertFromRequest(TrackRequest trackRequest, @MappingTarget Track track);
}
