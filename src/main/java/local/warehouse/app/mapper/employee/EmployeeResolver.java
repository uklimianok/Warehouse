package local.warehouse.app.mapper.employee;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.employee.Employee;
import local.warehouse.app.repository.employee.EmployeeRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeResolver {
    private final EmployeeRepository employeeRepository;

    public Employee mapEmployee(long employeeId) {
        return employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageHandler.getOutputMessage(Entity.EMPLOYEE, OutputMessage.NOT_FOUND)));
    }
}
