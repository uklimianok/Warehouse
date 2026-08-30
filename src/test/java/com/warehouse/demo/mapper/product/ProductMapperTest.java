package com.warehouse.demo.mapper.product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.warehouse.demo.dto.product.FullProductResponse;
import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.entity.product.Product;
import com.warehouse.demo.mapper.employee.organization.OrganizationResponseMapper;
import com.warehouse.demo.mapper.employee.organization.OrganizationResponseMapperImpl;
import com.warehouse.demo.mapper.employee.organizationType.OrganizationTypeResponseMapper;
import com.warehouse.demo.mapper.employee.organizationType.OrganizationTypeResponseMapperImpl;

public class ProductMapperTest {
    OrganizationTypeResponseMapper organizationTypeMapper = new OrganizationTypeResponseMapperImpl();
    OrganizationResponseMapper organizationMapper = new OrganizationResponseMapperImpl(organizationTypeMapper);
    ProductResponseMapper productMapper = new ProductResponseMapperImpl(organizationMapper);

    @Test
    void toResponse_product_returnsProductResponse() {
        Product product = create();
        FullProductResponse fullResponse = productMapper.toFullResponse(product);
        assertEquals("ORG1", fullResponse.getProducer().getOrganizationNumber());
        assertEquals("Producer", fullResponse.getProducer().getOrganizationType().getName());
    }

    private Product create() {
        Product product = new Product();
        product.setName("Product 1");
        product.setBarcodeNumber("123456789");
        product.setCost(new BigDecimal("4.99"));
        
        Organization producer = new Organization();
        producer.setName("Organization 1");
        producer.setOrganizationNumber("ORG1");
        producer.setAddress("Address 1");
        producer.setPhoneNumber("+123456789");
        producer.setEmail("org1@ex.com");
        producer.setUrl("org1.com");

        OrganizationType organizationType = new OrganizationType();
        organizationType.setName("Producer");

        producer.setOrganizationType(organizationType);
        product.setProducer(producer);

        return product;
    }
}
