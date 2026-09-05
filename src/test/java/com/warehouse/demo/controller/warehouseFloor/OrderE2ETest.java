package com.warehouse.demo.controller.warehouseFloor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeAll;
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

import com.warehouse.demo.dto.order.FullOrderResponse;
import com.warehouse.demo.dto.order.OrderRequest;
import com.warehouse.demo.dto.order.OrderResponse;
import com.warehouse.demo.dto.service.status.StatusResponse;
import com.warehouse.demo.dto.workplace.gate.GateRequest;
import com.warehouse.demo.dto.workplace.gate.GateResponse;
import com.warehouse.demo.entity.service.Status;
import com.warehouse.demo.repository.service.StatusRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.StatusInfo;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate 
@Testcontainers 
public class OrderE2ETest {
    @Container 
    @ServiceConnection 
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16");
    @LocalServerPort 
    private int port;
    @Autowired 
    private TestRestTemplate restTemplate;
    @Value("${warehouse.shared-password}")
    private String sharedPassword;

    @BeforeAll 
    static void seedStatuses(@Autowired StatusRepository statusRepository) {
        Status statusAccepted = new Status();
        statusAccepted.setName(StatusInfo.OrderStatus.ACCEPTED.getName());
        statusAccepted.setType(EntityName.ORDER.getName());
        statusRepository.save(statusAccepted);

        Status statusStarted = new Status();
        statusStarted.setName(StatusInfo.OrderStatus.STARTED.getName());
        statusStarted.setType(EntityName.ORDER.getName());
        statusRepository.save(statusStarted);
    }

    @Test 
    void create_notAcceptedStatusAsSystemAdministrator_returnsOk() {
        GateRequest gate = createGate();
        ResponseEntity<Void> gateCreateResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .postForEntity("http://localhost:" + port + "/gates", gate, Void.class);
        assertEquals(HttpStatus.CREATED, gateCreateResponse.getStatusCode());

        OrderRequest order = createOrder(2, null);
        ResponseEntity<? extends OrderResponse> createOrderResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .postForEntity("http://localhost:" + port + "/orders", order, FullOrderResponse.class);
        assertEquals(HttpStatus.CREATED, createOrderResponse.getStatusCode());
        assertEquals(StatusInfo.OrderStatus.ACCEPTED.getName(), ((FullOrderResponse) createOrderResponse.getBody()).getStatus().getName());
        assertNull(((FullOrderResponse) createOrderResponse.getBody()).getGate());
    }

    @Test
    void update_startedStatusAsSystemAdministrator_returnsConflict() {
        // 1. Read STARTED status
        ResponseEntity<StatusResponse> statusResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .getForEntity("http://localhost:" + port + "/statuses?name={name}&type={type}", StatusResponse.class, StatusInfo.OrderStatus.STARTED.getName(), EntityName.ORDER.getName());
        assertEquals(HttpStatus.OK, statusResponse.getStatusCode());
        long startedStatusId = statusResponse.getBody().getId();

        // 2. Create an Order (lands as ACCEPTED automatically)
        OrderRequest createOrderRequest = createOrder(startedStatusId, null); // statusId here is ignored by create(), per your earlier fix
        ResponseEntity<FullOrderResponse> createOrderResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .postForEntity("http://localhost:" + port + "/orders", createOrderRequest, FullOrderResponse.class);
        assertEquals(HttpStatus.CREATED, createOrderResponse.getStatusCode());
        long createdOrderId = createOrderResponse.getBody().getId();

        // 3. Attempt update: STARTED status + null gate -> should be rejected
        OrderRequest updateOrderRequest = createOrder(startedStatusId, null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderRequest> updateEntity = new HttpEntity<>(updateOrderRequest, headers);

        ResponseEntity<String> updateResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .exchange("http://localhost:" + port + "/orders/" + createdOrderId, HttpMethod.PATCH, updateEntity, String.class);
        assertEquals(HttpStatus.CONFLICT, updateResponse.getStatusCode());
    }

    @Test 
    void update_withGateAsSystemAdministrator_returnsOk() {
        // 1. Read STARTED status
        ResponseEntity<StatusResponse> statusResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .getForEntity("http://localhost:" + port + "/statuses?name={name}&type={type}", StatusResponse.class, StatusInfo.OrderStatus.STARTED.getName(), EntityName.ORDER.getName());
        assertEquals(HttpStatus.OK, statusResponse.getStatusCode());
        long startedStatusId = statusResponse.getBody().getId();

        // 2. Create a Gate
        GateRequest createGateRequest = createGate();
        ResponseEntity<? extends GateResponse> createGateResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .postForEntity("http://localhost:" + port + "/gates", createGateRequest, GateResponse.class);
        assertEquals(HttpStatus.CREATED, createGateResponse.getStatusCode());
        long createGateId = createGateResponse.getBody().getId();

        // 3. Create an Order (lands as ACCEPTED automatically)
        OrderRequest orderRequest = createOrder(startedStatusId, null); // statusId here is ignored by create(), per your earlier fix
        ResponseEntity<FullOrderResponse> createOrderResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .postForEntity("http://localhost:" + port + "/orders", orderRequest, FullOrderResponse.class);
        assertEquals(HttpStatus.CREATED, createOrderResponse.getStatusCode());
        long createdOrderId = createOrderResponse.getBody().getId();

        // 4. Attempt update: STARTED status + non-null gate
        orderRequest.setStatusId(startedStatusId);
        orderRequest.setGateId(createGateId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderRequest> updateEntity = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<? extends OrderResponse> updateOrderResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .exchange("http://localhost:" + port + "/orders/" + createdOrderId, HttpMethod.PATCH, updateEntity, FullOrderResponse.class);
        assertEquals(HttpStatus.OK, updateOrderResponse.getStatusCode());
        assertEquals(StatusInfo.OrderStatus.STARTED.getName(), ((FullOrderResponse) updateOrderResponse.getBody()).getStatus().getName());
        assertNotNull(updateOrderResponse.getBody().getGate());
    }

    private GateRequest createGate() {
        GateRequest gateRequest = new GateRequest();
        gateRequest.setSymbol("G-" + System.nanoTime());

        return gateRequest;
    }

    private OrderRequest createOrder(long statusId, Long gateId) {
        OrderRequest order = new OrderRequest();
        order.setStoreId(1);
        order.setShiftId(1);
        order.setStatusId(statusId);
        order.setGateId(gateId);

        return order;
    }
}
