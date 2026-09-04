package com.warehouse.demo.mapper.product;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.product.Product;
import com.warehouse.demo.repository.product.ProductRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductResolver {
    private final ProductRepository productRepository;

    public Product mapProduct(long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(EntityName.PRODUCT, OutputMessage.NOT_FOUND)));
    }
}
