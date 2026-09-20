package com.warehouse.demo.service.workplace;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.workplace.Workshop;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.workplace.WorkshopRepository;

@SpringBootTest 
@Testcontainers 
public class WorkshopRotationServiceIT {
    @Container 
    @ServiceConnection 
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:latest");

    @Autowired private WorkshopRotationService workshopRotationService;

    @Autowired private WorkshopRepository workshopRepository;
    @Autowired private EmployeeRepository employeeRepository;

    @Test 
    void writeAssignNextOperatorConflict_throwsException() {
        Workshop workshop = configureWorkshop();
        List<Employee> operators = configureOperators(workshop)
            .stream()
            .sorted((e1, e2) -> Long.compare(e1.getId(), e2.getId()))
            .toList();

        AtomicReference<Exception> exception1 = new AtomicReference<>();
        AtomicReference<Exception> exception2 = new AtomicReference<>();

        Runnable task1 = () -> {
            try {
                workshopRotationService.assignNextOperator(workshop.getId(), operators);
            } catch (Exception e) {
                exception1.set(e);
                System.out.println("Thread saw: " + e);
            }
        };
        Runnable task2 = () -> {
            try {
                workshopRotationService.assignNextOperator(workshop.getId(), operators);
            } catch (Exception e) {
                exception2.set(e);
                System.out.println("Thread saw: " + e);
            }
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        boolean atLeastOneConflict = 
            (exception1.get() instanceof ObjectOptimisticLockingFailureException)
            || (exception2.get() instanceof ObjectOptimisticLockingFailureException);

        assertTrue(atLeastOneConflict);
    }

    private Workshop configureWorkshop() {
        Workshop workshop = new Workshop();
        workshop.setName("Common products");
        workshop.setStandard(new BigDecimal("145.00"));
        return  workshopRepository.save(workshop);
    }

    private List<Employee> configureOperators(Workshop workshop) {
        Employee firstOperator = employeeRepository.findByEmployeeNumber("06000001").get(); // Already set in V23
        firstOperator.setWorkshop(workshop);

        Employee secondOperator = new Employee();
        secondOperator.setBirthDate(firstOperator.getBirthDate());
        secondOperator.setDocumentId(firstOperator.getDocumentId());
        secondOperator.setEmployerOrganization(firstOperator.getEmployerOrganization());
        secondOperator.setFirstName(firstOperator.getFirstName());
        secondOperator.setLastName(firstOperator.getLastName());
        secondOperator.setPhoneNumber(firstOperator.getPhoneNumber());
        secondOperator.setPosition(firstOperator.getPosition());
        secondOperator.setResidenceAddress(firstOperator.getResidenceAddress());
        secondOperator.setShift(firstOperator.getShift());
        secondOperator.setEmployeeNumber("02100027");
        secondOperator.setWorkshop(workshop);

        employeeRepository.save(firstOperator);
        employeeRepository.save(secondOperator);

        return employeeRepository.findAllByPositionCodeNameAndWorkshopId("OPERATOR", workshop.getId());
    }
}
