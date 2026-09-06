package com.warehouse.demo.controller.warehouseFloor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;

import com.warehouse.demo.configuration.security.ApplicationConfiguration;
import com.warehouse.demo.configuration.security.UserPrincipal;
import com.warehouse.demo.dto.employee.organization.FullOrganizationResponse;
import com.warehouse.demo.dto.employee.organizationType.OrganizationTypeResponse;
import com.warehouse.demo.dto.product.FullProductResponse;
import com.warehouse.demo.dto.product.ProductRequest;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.entity.employee.Position;
import com.warehouse.demo.entity.product.Product;
import com.warehouse.demo.entity.user.User;
import com.warehouse.demo.mapper.product.ProductResponseMapper;
import com.warehouse.demo.service.product.ProductService;

import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProductController.class)
@Import(ApplicationConfiguration.class)
public class ProductControllerTest {    // @WebMvcTest/MockMvc tests of controllers
    @Autowired private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @MockitoBean private ProductService productService;
    @MockitoBean private ProductResponseMapper productResponseMapper;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = {"TRUCK_DRIVER"})
    void read_truckDriverRole_returnsForbidden() throws Exception {
        mockMvc.perform(get("/products/1"))
            .andDo(print())                         // Print debug
            .andExpect(status().isForbidden());
    }

    @Test
    void read_dataControllerRole_returnsFullProductResponse() throws Exception {
        UserPrincipal principal = generatePrincipal("DATA_CONTROLLER");
        Product product = generateProduct();
        FullProductResponse productResponse = generateProductResponse(product);

        when(productService.read(1L)).thenReturn(product);
        when(productResponseMapper.convertToFullResponse(product)).thenReturn(productResponse);

        mockMvc.perform(get("/products/1").with(user(principal)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Product 1"))
            .andExpect(jsonPath("$.barcodeNumber").value("987654321"))
            .andExpect(jsonPath("$.producer.name").value("Organization 1"));
    }

    @Test
    void create_dataControllerRole_returnsFullProductResponse() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Product 1");
        productRequest.setBarcodeNumber("987654321");
        productRequest.setCost(new BigDecimal("4.99"));
        productRequest.setProducerId(1);

        UserPrincipal principal = generatePrincipal("DATA_CONTROLLER");
        Product product = generateProduct();
        FullProductResponse productResponse = generateProductResponse(product);

        when(productService.create(any(ProductRequest.class))).thenReturn(product);
        when(productResponseMapper.convertToFullResponse(product)).thenReturn(productResponse);

        mockMvc.perform(post("/products")
                .with(user(principal))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Product 1"))
            .andExpect(jsonPath("$.barcodeNumber").value("987654321"))
            .andExpect(jsonPath("$.producer.name").value("Organization 1"));

        verify(productService).create(any(ProductRequest.class));
    }

    private UserPrincipal generatePrincipal(String roleCode) {
        Position position = new Position();
        
        Employee employee = new Employee();
        employee.setPosition(position);

        if (roleCode.equals("DATA_CONTROLLER")) {
            position.setCodeName("DATA_CONTROLLER");
            employee.setEmployeeNumber("09100001");
        }

        User user = new User();
        user.setEmployee(employee);
        user.setPassword("test");  // Avoids real authentication
        user.setEnabled(true);

        return new UserPrincipal(user);
    }

    private Product generateProduct() {
        OrganizationType organizationType = new OrganizationType();
        organizationType.setId(1);
        organizationType.setName("Producer");

        Organization producer = new Organization();
        producer.setId(1);
        producer.setName("Organization 1");
        producer.setOrganizationNumber("ORG001");
        producer.setOrganizationType(organizationType);
        producer.setAddress("Address 1");
        producer.setPhoneNumber("+123456789");
        producer.setEmail("org1@ex.com");
        producer.setUrl("org1.com");

        return generateProduct(1, "Product 1", "987654321", new BigDecimal("4.99"), producer);
    }

    private Product generateProduct(long id, String name, String barcodeNumber, BigDecimal cost, Organization producer) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setBarcodeNumber(barcodeNumber);
        product.setCost(cost);    // String avoids the machinery zero
        product.setProducer(producer);

        return product;
    }

    private FullProductResponse generateProductResponse(Product product) {
        OrganizationTypeResponse organizationTypeResponse = new OrganizationTypeResponse();
        organizationTypeResponse.setName(product.getProducer().getOrganizationType().getName());

        FullOrganizationResponse organizationResponse = new FullOrganizationResponse();
        organizationResponse.setName(product.getProducer().getName());
        organizationResponse.setOrganizationNumber(product.getProducer().getOrganizationNumber());
        organizationResponse.setOrganizationType(organizationTypeResponse);
        organizationResponse.setAddress(product.getProducer().getAddress());
        organizationResponse.setEmail(product.getProducer().getEmail());
        organizationResponse.setPhoneNumber(product.getProducer().getPhoneNumber());
        organizationResponse.setUrl(product.getProducer().getUrl());

        FullProductResponse productResponse = new FullProductResponse();
        productResponse.setName(product.getName());
        productResponse.setBarcodeNumber(product.getBarcodeNumber());
        productResponse.setCost(product.getCost());
        productResponse.setProducer(organizationResponse);

        return productResponse;
    }
}
