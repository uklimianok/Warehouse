package com.warehouse.demo.service.workplace;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.entity.employee.Shift;
import com.warehouse.demo.entity.item.Pallet;
import com.warehouse.demo.entity.product.Product;
import com.warehouse.demo.entity.product.ProductPackage;
import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.entity.service.Status;
import com.warehouse.demo.entity.workplace.WorkStation;
import com.warehouse.demo.entity.workplace.Workshop;
import com.warehouse.demo.event.product.ProductPalletEvent;
import com.warehouse.demo.mapper.product.productPallet.ProductPalletResponseMapper;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.product.ProductPalletRepository;
import com.warehouse.demo.repository.workplace.WorkshopRepository;
import com.warehouse.demo.util.info.Department;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.StatusInfo;

@ExtendWith(MockitoExtension.class)
public class WorkshopRotationServiceTest {
    private WorkshopRotationService service;

    @Mock WorkshopRotationService self;

    @Mock private ProductPalletRepository productPalletRepository;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private WorkshopRepository workshopRepository;

    @Mock private ProductPalletResponseMapper productPalletResponseMapper;

    @Mock private SimpMessagingTemplate simpMessagingTemplate;

    @BeforeEach 
    void init() {
        service = new WorkshopRotationService(self, productPalletRepository, employeeRepository, workshopRepository, productPalletResponseMapper, simpMessagingTemplate);
    }

    @Test 
    void repeatAttemptsToSendData() {
        ProductPallet productPallet = getProductPallet();
        Workshop workshop = productPallet.getNextWorkStation().getWorkshop();
        List<Employee> operators = getEmployees(workshop);

        when(productPalletRepository.findByPalletNumber("000000000001"))
            .thenReturn(Optional.of(productPallet));
        when(employeeRepository.findAllByPositionCodeNameAndWorkshopId("OPERATOR", workshop.getId()))
            .thenReturn(operators);
        when(self.assignNextOperator(workshop.getId(), operators))
            .thenThrow(new ObjectOptimisticLockingFailureException(Workshop.class, 1L))
            .thenThrow(new ObjectOptimisticLockingFailureException(Workshop.class, 1L))
            .thenReturn(operators.get(0));

        ProductPalletEvent productPalletEvent = new ProductPalletEvent();
        productPalletEvent.setGroupNumber(productPallet.getGroupNumber());
        productPalletEvent.setPalletNumber(productPallet.getPalletNumber());
        productPalletEvent.setProductPackageId(productPallet.getProductPackage().getId());
        productPalletEvent.setWorkStationId(
            productPallet.getWorkStation() == null ?
            null :
            productPallet.getWorkStation().getId()
        );
        productPalletEvent.setNextWorkStationId(
            productPallet.getNextWorkStation() == null ?
            null :
            productPallet.getNextWorkStation().getId()
        );
        service.consumeEvent(productPalletEvent);

        verify(self, times(3)).assignNextOperator(workshop.getId(), operators);
    }

    private ProductPallet getProductPallet() {
        Pallet pallet = new Pallet();
        pallet.setId(1);
        pallet.setName("Euro");
        pallet.setColor("Red");
        pallet.setLength(new BigDecimal("1.20"));
        pallet.setWidth(new BigDecimal("1.00"));
        pallet.setHeight(new BigDecimal("0.20"));
        pallet.setWeight(new BigDecimal("25.00"));

        Workshop workshop = new Workshop();
        workshop.setId(1);
        workshop.setName("Common products");
        workshop.setStandard(new BigDecimal("145.00"));

        WorkStation workStation = new WorkStation();
        workStation.setId(1);
        workStation.setControlNumber("38");
        workStation.setStationNumber("01-001");
        workStation.setType("active");
        workStation.setWorkshop(workshop);

        WorkStation nextWorkStation = new WorkStation();
        nextWorkStation.setId(2);
        nextWorkStation.setControlNumber("42");
        nextWorkStation.setStationNumber("01-002");
        nextWorkStation.setType("active");
        nextWorkStation.setWorkshop(workshop);

        Organization organization = getOrganization();

        Product product = new Product();
        product.setId(1);
        product.setBarcodeNumber("123456789");
        product.setCost(new BigDecimal("0.49"));
        product.setName("Product 1");
        product.setProducer(organization);
        
        ProductPackage productPackage = new ProductPackage();
        productPackage.setId(1);
        productPackage.setProduct(product);
        productPackage.setProductsAmount(20);
        productPackage.setVolume(new BigDecimal("5.00"));
        productPackage.setWeight(new BigDecimal("120.00"));

        Status status = new Status();
        status.setId(1);
        status.setName(StatusInfo.PRODUCT_PALLET_UNLOADED);
        status.setType(Entity.PRODUCT_PALLET.getEntity());

        ProductPallet productPallet = new ProductPallet();
        productPallet.setId(1);
        productPallet.setGroupNumber("00001");
        productPallet.setPackageAmount(10);
        productPallet.setPallet(pallet);
        productPallet.setPalletNumber("000000000001");
        productPallet.setProductPackage(productPackage);
        productPallet.setWorkStation(workStation);
        productPallet.setNextWorkStation(nextWorkStation);
        productPallet.setStatus(status);

        return productPallet;
    }

    private Employee getEmployee(Workshop workshop) {
        Position position = new Position();
        position.setId(1);
        position.setCodeName("OPERATOR");
        position.setDepartment(Department.WAREHOUSE_EMPLOYEES_DEPARTMENT);
        position.setHasDatabaseAccess(true);
        position.setName("Position");

        Shift shift = new Shift();
        shift.setId(1);
        shift.setSymbol("1");

        Employee employee = new Employee();
        employee.setId(1);
        employee.setEmployeeNumber("06000001");
        employee.setEmployerOrganization(getOrganization());
        employee.setFirstName("FirstName");
        employee.setLastName("LastName");
        employee.setPosition(position);
        employee.setShift(shift);
        employee.setWorkshop(workshop);

        return employee;
    }

    List<Employee> getEmployees(Workshop workshop) {
        return List.of(getEmployee(workshop));
    }

    private Organization getOrganization() {
        OrganizationType organizationType = new OrganizationType();
        organizationType.setId(1);
        organizationType.setName("Producer");

        Organization organization = new Organization();
        organization.setId(1);
        organization.setName("Organization 1");
        organization.setOrganizationNumber("ORG1");
        organization.setOrganizationType(organizationType);

        return organization;
    }
}
