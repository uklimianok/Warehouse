package local.warehouse.app.service.employee;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import local.warehouse.app.event.employee.EmployeeUpdatedEvent;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class EmployeeEventListener {
    private final EmployeeService employeeService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onEmployeeUpdated(EmployeeUpdatedEvent employeeUpdatedEvent) {
        employeeService.sendPendingNotifications(employeeUpdatedEvent.employeeNumber());
    }
}
