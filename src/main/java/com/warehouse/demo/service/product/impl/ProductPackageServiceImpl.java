package com.warehouse.demo.service.product.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.product.productPackage.ProductPackageRequest;
import com.warehouse.demo.entity.product.ProductPackage;
import com.warehouse.demo.mapper.product.productPackage.ProductPackageRequestMapper;
import com.warehouse.demo.repository.order.OrderedProductRepository;
import com.warehouse.demo.repository.order.PickedProductRepository;
import com.warehouse.demo.repository.product.ProductPackageRepository;
import com.warehouse.demo.repository.product.ProductPalletRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.product.ProductPackageService;
import com.warehouse.demo.util.EntityName;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductPackageServiceImpl extends AbstractService<ProductPackage, Long> implements ProductPackageService {
    private final ProductPackageRepository productPackageRepository;
    private final ProductPalletRepository productPalletRepository;
    private final OrderedProductRepository orderedProductRepository;
    private final PickedProductRepository pickedProductRepository;

    private final ProductPackageRequestMapper productPackageRequestMapper;

    @Override
    public ProductPackage create(ProductPackageRequest productPackageRequest) {
        ProductPackage productPackage = new ProductPackage();
        return modifyAndSave(productPackage, productPackageRequest);
    }

    @Override
    public ProductPackage update(long id, ProductPackageRequest productPackageRequest) {
        ProductPackage productPackage = read(id);
        return modifyAndSave(productPackage, productPackageRequest);
    }

    @Override
    protected JpaRepository<ProductPackage, Long> getRepository() {
        return productPackageRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.PRODUCT_PACKAGE;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInPallet = productPalletRepository.existsByProductPackageId(id);
        boolean activeInOrderedProduct = orderedProductRepository.existsByProductPackageId(id);
        boolean activeInPickedProduct = pickedProductRepository.existsByProductPackageId(id);
        return activeInPallet || activeInOrderedProduct || activeInPickedProduct;
    }

    private ProductPackage modifyAndSave(ProductPackage target, ProductPackageRequest from) {
        productPackageRequestMapper.convertFromRequest(from, target);
        return productPackageRepository.save(target);
    }
}
