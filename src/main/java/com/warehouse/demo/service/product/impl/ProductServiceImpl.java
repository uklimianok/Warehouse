package com.warehouse.demo.service.product.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.product.ProductRequest;
import com.warehouse.demo.entity.product.Product;
import com.warehouse.demo.mapper.product.ProductRequestMapper;
import com.warehouse.demo.repository.order.ReturnProductRepository;
import com.warehouse.demo.repository.product.ProductPackageRepository;
import com.warehouse.demo.repository.product.ProductRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.product.ProductService;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends AbstractService<Product, Long> implements ProductService {
    private final ProductRepository productRepository;
    private final ProductPackageRepository productPackageRepository;
    private final ReturnProductRepository returnProductRepository;

    private final ProductRequestMapper productRequestMapper;

    @Override 
    @Cacheable(value = "products", key = "#id")
    public Product read(Long id) {
        return super.read(id);
    }

    @Override
    public Product create(ProductRequest productRequest) {
        if (productRepository.existsByBarcodeNumber(productRequest.getBarcodeNumber()))
            throw new DataIntegrityViolationException(Utility.getOutputMessage(Entity.BARCODE_NUMBER, OutputMessage.EXISTS));
        
        return modifyAndSave(new Product(), productRequest);
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public Product update(long id, ProductRequest productRequest) {
        Product product = read(id);
        boolean barcodeNumberChanged = !product.getBarcodeNumber().equals(productRequest.getBarcodeNumber());
        boolean barcodeNumberExists = productRepository.existsByBarcodeNumber(productRequest.getBarcodeNumber());
        if (barcodeNumberChanged && barcodeNumberExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(Entity.BARCODE_NUMBER, OutputMessage.EXISTS));

        return modifyAndSave(product, productRequest);
    }

    @Override 
    @CacheEvict(value = "products", key = "#id")
    public void delete(Long id) {
        super.delete(id);
    }

    private Product modifyAndSave(Product target, ProductRequest from) {
        productRequestMapper.convertFromRequest(from, target);
        return productRepository.save(target);
    }

    @Override
    protected JpaRepository<Product, Long> getRepository() {
        return productRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.PRODUCT;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInProductPackage = productPackageRepository.existsByProductId(id);
        boolean activeInReturnProduct = returnProductRepository.existsByProductId(id);
        return activeInProductPackage || activeInReturnProduct;
    }
}
