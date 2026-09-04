package com.warehouse.demo.mapper.product.productPackage;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.product.ProductPackage;
import com.warehouse.demo.repository.product.ProductPackageRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductPackageResolver {
    private final ProductPackageRepository productPackageRepository;
    
    public ProductPackage mapProductPackage(long productPackageId) {
        return productPackageRepository.findById(productPackageId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(EntityName.PRODUCT_PACKAGE, OutputMessage.NOT_FOUND)));
    }
}
