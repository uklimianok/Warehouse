package local.warehouse.app.mapper.employee.department;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import local.warehouse.app.dto.employee.department.DepartmentResponse;
import local.warehouse.app.dto.employee.department.FullDepartmentResponse;
import local.warehouse.app.entity.employee.Department;

@Mapper(
    componentModel = "spring", 
    builder = @Builder(disableBuilder = true)
)
public interface DepartmentResponseMapper {
    DepartmentResponse convertToResponse(Department department);
    FullDepartmentResponse convertToFullResponse(Department department);
}
