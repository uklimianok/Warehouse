package com.warehouse.demo.controller.officeManagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.warehouse.demo.dto.employee.EmployeeRequest;
import com.warehouse.demo.dto.employee.FullEmployeeResponse;
import com.warehouse.demo.repository.employee.PositionRepository;
import com.warehouse.demo.repository.user.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate 
@Testcontainers 
public class EmployeeE2ETest {
    @Container 
    @ServiceConnection 
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16");

    @LocalServerPort private int port;
    @Autowired private TestRestTemplate restTemplate;
    @Autowired private UserRepository userRepository;
    @Autowired private PositionRepository positionRepository;

    @Value("${warehouse.shared-password}")
    private String sharedPassword;

    @Test 
    void create_databaseAccessPosition_createUserRow() {
        long goodsUnloaderPositionId = positionRepository
            .findByCodeName("GOODS_UNLOADER")
            .orElseThrow()
            .getId();

        EmployeeRequest employeeRequest = createEmployee(goodsUnloaderPositionId);
        ResponseEntity<FullEmployeeResponse> employeeResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .postForEntity("http://localhost:" + port + "/employees", employeeRequest, FullEmployeeResponse.class);
        assertEquals(HttpStatus.CREATED, employeeResponse.getStatusCode());
        long createEmployeeId = employeeResponse.getBody().getId();

        boolean userExists = userRepository.existsByEmployeeId(createEmployeeId);
        assertTrue(userExists);
    }

    @Test 
    void create_noDatabaseAccessPosition_createUserRow() {
        long truckDriverPositionId = positionRepository
            .findByCodeName("TRUCK_DRIVER")
            .orElseThrow()
            .getId();

        EmployeeRequest employeeRequest = createEmployee(truckDriverPositionId);
        ResponseEntity<FullEmployeeResponse> employeeResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .postForEntity("http://localhost:" + port + "/employees", employeeRequest, FullEmployeeResponse.class);
        assertEquals(HttpStatus.CREATED, employeeResponse.getStatusCode());
        long createEmployeeId = employeeResponse.getBody().getId();

        boolean userExists = userRepository.existsByEmployeeId(createEmployeeId);
        assertFalse(userExists);
    }

    @Test 
    void update_revokeDatabaseAccess_deleteUserRow() {
        long goodsUnloaderPositionId = positionRepository.findByCodeName("GOODS_UNLOADER").orElseThrow().getId();
        long truckDriverPositionId = positionRepository.findByCodeName("TRUCK_DRIVER").orElseThrow().getId();

        EmployeeRequest employeeRequest = createEmployee(goodsUnloaderPositionId);
        ResponseEntity<FullEmployeeResponse> employeeResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .postForEntity("http://localhost:" + port + "/employees", employeeRequest, FullEmployeeResponse.class);
        assertEquals(HttpStatus.CREATED, employeeResponse.getStatusCode());
        long createEmployeeId = employeeResponse.getBody().getId();

        employeeRequest.setPositionId(truckDriverPositionId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmployeeRequest> updateEntity = new HttpEntity<>(employeeRequest, headers);

        ResponseEntity<FullEmployeeResponse> updateEmployeeResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .exchange("http://localhost:" + port + "/employees/" + createEmployeeId, HttpMethod.PATCH, updateEntity, FullEmployeeResponse.class);
        assertEquals(HttpStatus.OK, updateEmployeeResponse.getStatusCode());
        assertFalse(userRepository.existsByEmployeeId(createEmployeeId));
    }

    @Test 
    void update_grantDatabaseAccess_createUserRow() {
        long goodsUnloaderPositionId = positionRepository.findByCodeName("GOODS_UNLOADER").orElseThrow().getId();
        long truckDriverPositionId = positionRepository.findByCodeName("TRUCK_DRIVER").orElseThrow().getId();

        EmployeeRequest employeeRequest = createEmployee(truckDriverPositionId);
        ResponseEntity<FullEmployeeResponse> employeeResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .postForEntity("http://localhost:" + port + "/employees", employeeRequest, FullEmployeeResponse.class);
        assertEquals(HttpStatus.CREATED, employeeResponse.getStatusCode());
        long createEmployeeId = employeeResponse.getBody().getId();

        employeeRequest.setPositionId(goodsUnloaderPositionId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmployeeRequest> updateEntity = new HttpEntity<>(employeeRequest, headers);

        ResponseEntity<FullEmployeeResponse> updateEmployeeResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .exchange("http://localhost:" + port + "/employees/" + createEmployeeId, HttpMethod.PATCH, updateEntity, FullEmployeeResponse.class);
        assertEquals(HttpStatus.OK, updateEmployeeResponse.getStatusCode());
        assertTrue(userRepository.existsByEmployeeId(createEmployeeId));
    }

    private EmployeeRequest createEmployee(long positionId) {
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("Name 2");
        employeeRequest.setLastName("Last name 2");
        employeeRequest.setEmployerOrganizationId(1);
        employeeRequest.setPositionId(positionId);
        employeeRequest.setShiftId(1);
        employeeRequest.setDocumentId("DOC002");
        employeeRequest.setBirthDate(LocalDate.now());
        employeeRequest.setResidenceAddress("Address 002");
        employeeRequest.setPhoneNumber("+2000");

        return employeeRequest;
    }
}
