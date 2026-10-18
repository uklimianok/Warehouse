package local.warehouse.app.mapper.employee.department;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.employee.Department;
import local.warehouse.app.repository.employee.DepartmentRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class DepartmentResolver {
    private final DepartmentRepository departmentRepository;

    public Department mapDepartment(long departmentId) {
        return departmentRepository.findById(departmentId)
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.DEPARTMENT, OutputMessage.NOT_FOUND)));
    }
}
