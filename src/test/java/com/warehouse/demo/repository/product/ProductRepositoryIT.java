package com.warehouse.demo.repository.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.entity.product.Product;
import com.warehouse.demo.repository.employee.OrganizationRepository;
import com.warehouse.demo.repository.employee.OrganizationTypeRepository;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest     // Launches whole app context
@Testcontainers     // Scans @Container-annotated fields
public class ProductRepositoryIT {
    @Container          // Testcontainers manages lifecycle of the field
    @ServiceConnection  // Overrides DB connection configuration
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16");   // Loads postgres image of version 16

    @Autowired private ProductRepository productRepository;
    @Autowired private OrganizationRepository organizationRepository;
    @Autowired private OrganizationTypeRepository organizationTypeRepository;

    @Test
    void contextLoadsAndDatabaseIsReachable() {
        long count = productRepository.count();
        assertEquals(0, count);
    }

    @Test
    void save_product_returnsProduct() {
        Product product = create();
        Product savedProduct = save(product);

        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getProducer().getName(), savedProduct.getProducer().getName());
        assertEquals(product.getProducer().getOrganizationType().getName(), savedProduct.getProducer().getOrganizationType().getName());
    }

    @Test
    void existsByBarcodeNumber_savedBarcode_returnsTrue() {
        save(create());

        boolean exists = productRepository.existsByBarcodeNumber("123456789");
        assertTrue(exists);
    }

    @Test
    void existsByBarcodeNumber_unsavedBarcode_returnsFalse() {
        save(create());

        boolean exists = productRepository.existsByBarcodeNumber("123456780");
        assertFalse(exists);
    }

    @Test
    void save_duplicateBarcodeNumber_throwsException() {
        save(create());

        Product duplicate = create();
        
        assertThrows(DataIntegrityViolationException.class, () -> save(duplicate));
    }

    private Product create() {
        Product product = new Product();
        product.setName("Product 2");
        product.setBarcodeNumber("123456789");
        product.setCost(new BigDecimal("4.99"));
        
        Organization producer = new Organization();
        producer.setName("Organization 2");
        producer.setOrganizationNumber("ORG2");
        producer.setAddress("Address 1");
        producer.setPhoneNumber("+123456789");
        producer.setEmail("org1@ex.com");
        producer.setUrl("org1.com");

        OrganizationType organizationType = new OrganizationType();
        organizationType.setName("Producer 2");

        producer.setOrganizationType(organizationType);
        product.setProducer(producer);

        return product;
    }
    
    private Product save(Product product) {
        organizationTypeRepository.save(product.getProducer().getOrganizationType());
        organizationRepository.save(product.getProducer());
        return productRepository.save(product);
    }
}
