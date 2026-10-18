package local.warehouse.app.mapper.service.status;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import local.warehouse.app.dto.service.status.StatusRequest;
import local.warehouse.app.entity.service.Status;

@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface StatusRequestMapper {
    @Mapping(target = "id", ignore = true)
    void convertFromRequest(StatusRequest statusRequest, @MappingTarget Status status);
}
