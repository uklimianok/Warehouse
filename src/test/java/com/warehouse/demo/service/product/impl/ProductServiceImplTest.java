package com.warehouse.demo.service.product.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.warehouse.demo.dto.product.ProductRequest;
import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.entity.product.Product;
import com.warehouse.demo.repository.employee.OrganizationRepository;
import com.warehouse.demo.repository.order.ReturnProductRepository;
import com.warehouse.demo.repository.product.ProductPackageRepository;
import com.warehouse.demo.repository.product.ProductRepository;

@ExtendWith(MockitoExtension.class) // Enables @Mock and @InjectMocks
public class ProductServiceImplTest {   // Mockito tests of services
    @Mock private ProductRepository productRepository;
    @Mock private OrganizationRepository organizationRepository;
    @Mock private ProductPackageRepository productPackageRepository;
    @Mock private ReturnProductRepository returnProductRepository;

    @InjectMocks ProductServiceImpl productServiceImpl;

    @Test
    public void create_duplicateBarcodeNumber_throwsDataIntegrityViolationException() {
        ProductRequest request = new ProductRequest();
        request.setBarcodeNumber("123456789");

        when(productRepository.existsByBarcodeNumber("123456789")).thenReturn(true);

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            productServiceImpl.create(request);
        });
        String expected = "Barcode number already exists.";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void create_newProduct_returnsProduct() {
        ProductRequest request = new ProductRequest();
        request.setName("Product 1");
        request.setBarcodeNumber("123456789");
        request.setCost(new BigDecimal(5.));
        request.setProducerId(1);

        Organization producer = new Organization();
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(producer));
        when(productRepository.existsByBarcodeNumber("123456789")).thenReturn(false);   // Optionally
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product product = productServiceImpl.create(request);
        assertNotNull(product);
        assertEquals("123456789", product.getBarcodeNumber());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void update_barcodeChangedAndExists_throwsException() {
        ProductRequest request = new ProductRequest();
        request.setBarcodeNumber("123456789");

        Product oldProduct = new Product();
        oldProduct.setBarcodeNumber("123456780");

        when(productRepository.existsById(1L)).thenReturn(true);                                    // Old object exists
        when(productRepository.findById(1L)).thenReturn(Optional.of(oldProduct));                   // Return old object
        when(productRepository.existsByBarcodeNumber("123456789")).thenReturn(true);     // Barcode exists

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            productServiceImpl.update(1L, request);
        });
        String expected = "Barcode number already exists.";
        assertEquals(expected, exception.getMessage());
    }
    
    @Test
    public void update_barcodeChangedAndAvailable_returnsProduct() {
        ProductRequest request = new ProductRequest();
        request.setBarcodeNumber("123456789");
        request.setProducerId(1);

        Product oldProduct = new Product();
        oldProduct.setBarcodeNumber("123456780");

        Organization producer = new Organization();

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(oldProduct));
        when(productRepository.existsByBarcodeNumber("123456789")).thenReturn(false);   // Barcode available
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(producer));               // Producer exists, avoid NotFound
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));        // Return saved fake object

        Product newProduct = productServiceImpl.update(1, request);
        assertNotNull(newProduct);
        assertEquals("123456789", newProduct.getBarcodeNumber());                                      // Check fake saving
        verify(productRepository).save(any(Product.class));                                            // Verify if save() was ever called
    }

    @Test
    public void update_barcodeUnchanged_returnsProduct() {
        ProductRequest request = new ProductRequest();
        request.setBarcodeNumber("123456789");
        request.setProducerId(2);

        Product oldProduct = new Product();
        oldProduct.setBarcodeNumber("123456789");
        
        Organization producer = new Organization();
        producer.setId(2);

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(oldProduct));
        when(productRepository.existsByBarcodeNumber("123456789")).thenReturn(true);
        when(organizationRepository.findById(2L)).thenReturn(Optional.of(producer));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product newProduct = productServiceImpl.update(1, request);
        assertNotNull(newProduct);
        assertEquals("123456789", newProduct.getBarcodeNumber());
        assertEquals(2L, newProduct.getProducer().getId());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void isUsed_activeInOne_returnsTrue() {
        when(productPackageRepository.existsByProductId(1)).thenReturn(true);
        boolean actual = productServiceImpl.isUsed(1L);
        assertTrue(actual);
    }

    @Test
    public void isUsed_activeInOther_returnsTrue() {
        when(returnProductRepository.existsByProductId(1)).thenReturn(true);
        boolean actual = productServiceImpl.isUsed(1L);
        assertTrue(actual);
    }

    @Test
    public void isUsed_inactive_returnsFalse() {
        when(productPackageRepository.existsByProductId(1)).thenReturn(false);
        when(returnProductRepository.existsByProductId(1)).thenReturn(false);
        boolean actual = productServiceImpl.isUsed(1L);
        assertFalse(actual);
    }
}
