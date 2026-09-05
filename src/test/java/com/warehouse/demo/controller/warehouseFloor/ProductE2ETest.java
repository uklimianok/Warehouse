package com.warehouse.demo.controller.warehouseFloor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

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
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.warehouse.demo.dto.product.FullProductResponse;
import com.warehouse.demo.dto.product.ProductRequest;
import com.warehouse.demo.dto.product.ProductResponse;
import com.warehouse.demo.dto.product.productPackage.ProductPackageRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public class ProductE2ETest {
    @Container
    @ServiceConnection 
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16");
    @LocalServerPort 
    private int port;
    @Autowired 
    private TestRestTemplate restTemplate;
    @Value("${warehouse.shared-password}")
    private String sharedPassword;

    @Test 
    void readAll_asSystemAdministrator_returnsOk() {
        ResponseEntity<String> response = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .getForEntity("http://localhost:" + port + "/products", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test 
    void readAll_asTruckDriver_returnsForbidden() {
        ResponseEntity<String> response = restTemplate
            .withBasicAuth("01000001", sharedPassword)
            .getForEntity("http://localhost:" + port + "/products", String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test 
    void create_asDataController_returnsCreatedProduct() {
        ResponseEntity<? extends ProductResponse> response = save(create());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Product 1", response.getBody().getName());
        assertEquals(1, ((FullProductResponse) response.getBody()).getProducer().getId());
    }

    @Test 
    void read_asGoodsPicker_returnsProductResponse() {
        ResponseEntity<? extends ProductResponse> createResponse = save(create());
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        long createdId = createResponse.getBody().getId();

        ResponseEntity<? extends ProductResponse> response = restTemplate
            .withBasicAuth("03000001", sharedPassword)
            .getForEntity("http://localhost:" + port + "/products/" + createdId, FullProductResponse.class);   // Decoy
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(((FullProductResponse) response.getBody()).getProducer());   // Actually contains nothing for 03000001
    }

    @Test 
    void update_asDataController_returnsFullProductResponse() {
        ProductRequest createRequest = create();

        ResponseEntity<? extends ProductResponse> createResponse = save(createRequest);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        long createdId = createResponse.getBody().getId();

        createRequest.setName("Product 2");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductRequest> updateRequest = new HttpEntity<>(createRequest, headers);

        ResponseEntity<? extends ProductResponse> updateResponse = restTemplate
            .withBasicAuth("09000001", sharedPassword)
            .exchange("http://localhost:" + port + "/products/" + createdId, HttpMethod.PATCH, updateRequest, FullProductResponse.class);  // No patchForEntity method
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals(createRequest.getName(), updateResponse.getBody().getName());
    }

    @Test 
    void delete_asSystemAdministrator_returnsOk() {
        ProductRequest createRequest = create();

        ResponseEntity<? extends ProductResponse> createResponse = save(createRequest);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        long createdId = createResponse.getBody().getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);

        ResponseEntity<Void> deleteResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .exchange("http://localhost:" + port + "/products/" + createdId, HttpMethod.DELETE, deleteRequest, Void.class);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<String> readRawResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .getForEntity("http://localhost:" + port + "/products/" + createdId, String.class);
        assertEquals(HttpStatus.NOT_FOUND, readRawResponse.getStatusCode());
    }

    @Test 
    void delete_activeInProductPackageAsSystemAdministrator_returnsConflict() {
        ProductRequest createRequest = create();

        ResponseEntity<? extends ProductResponse> createResponse = save(createRequest);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        long createdId = createResponse.getBody().getId();

        ProductPackageRequest productPackageRequest = new ProductPackageRequest();
        productPackageRequest.setProductId(createdId);
        productPackageRequest.setProductsAmount(10);
        productPackageRequest.setVolume(new BigDecimal("10.000"));
        productPackageRequest.setWeight(new BigDecimal("5.266"));

        ResponseEntity<Void> productPackageResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .postForEntity("http://localhost:" + port + "/packages", productPackageRequest, Void.class);
        assertEquals(HttpStatus.CREATED, productPackageResponse.getStatusCode());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);

        ResponseEntity<Void> deleteResponse = restTemplate
            .withBasicAuth("24000001", sharedPassword)
            .exchange("http://localhost:" + port + "/products/" + createdId, HttpMethod.DELETE, deleteRequest, Void.class);
        assertEquals(HttpStatus.CONFLICT, deleteResponse.getStatusCode());
    }

    private ProductRequest create() {
        ProductRequest request = new ProductRequest();
        request.setName("Product 1");
        request.setBarcodeNumber("BC-" + System.nanoTime());
        request.setCost(new BigDecimal("4.99"));
        request.setProducerId(1);

        return request;
    }

    private ResponseEntity<? extends ProductResponse> save(ProductRequest request) {
        ResponseEntity<? extends ProductResponse> createResponse = restTemplate
            .withBasicAuth("09000001", sharedPassword)
            .postForEntity("http://localhost:" + port + "/products", request, FullProductResponse.class);

        return createResponse;
    }
}
