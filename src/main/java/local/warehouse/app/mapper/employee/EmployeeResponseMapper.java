package local.warehouse.app.mapper.employee;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.employee.DataControllerEmployeeResponse;
import local.warehouse.app.dto.employee.EmployeeResponse;
import local.warehouse.app.dto.employee.FullEmployeeResponse;
import local.warehouse.app.entity.employee.Employee;
import local.warehouse.app.mapper.employee.organization.OrganizationResponseMapper;
import local.warehouse.app.mapper.employee.position.PositionResponseMapper;
import local.warehouse.app.mapper.employee.shift.ShiftResponseMapper;

@Mapper(
    componentModel = "spring", 
    uses = {OrganizationResponseMapper.class, PositionResponseMapper.class, ShiftResponseMapper.class}, 
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface EmployeeResponseMapper {
    EmployeeResponse convertToResponse(Employee employee);
    DataControllerEmployeeResponse convertToDataControllerResponse(Employee employee);
    FullEmployeeResponse convertToFullResponse(Employee employee);
}
