package com.warehouse.demo.mapper.product.productPackage;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.product.ProductPackage;
import com.warehouse.demo.repository.product.ProductPackageRepository;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductPackageResolver {
    private final ProductPackageRepository productPackageRepository;
    
    public ProductPackage mapProductPackage(long productPackageId) {
        return productPackageRepository.findById(productPackageId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(Entity.PRODUCT_PACKAGE, OutputMessage.NOT_FOUND)));
    }
}
